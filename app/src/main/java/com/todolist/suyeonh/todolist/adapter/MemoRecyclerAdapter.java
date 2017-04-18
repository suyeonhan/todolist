package com.todolist.suyeonh.todolist.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.todolist.suyeonh.todolist.R;
import com.todolist.suyeonh.todolist.models.Memo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by gkstn on 2017-03-20.
 */

public class MemoRecyclerAdapter extends RealmRecyclerViewAdapter<Memo, MemoRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private TextView mMemoTextView;

    public void delete(int adapterPosition) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Memo memo = getItem(adapterPosition);
        memo.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public MemoRecyclerAdapter(Context context, @Nullable OrderedRealmCollection<Memo> data) {
        super(data, true);
        setHasStableIds(true);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memo, parent, false);

        mMemoTextView = (TextView) itemView.findViewById(R.id.memo_textview);
        return new ViewHolder(itemView);
    }

    // 그룹 삭제 팝업
    @Subscribe
    public void MemoDeleteDialog(final int position) {
        //View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("메모를 삭제합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                getData().deleteFromRealm(position);

                realm.commitTransaction();
                realm.close();

            }
        });
        builder.setNegativeButton("취소", null);
        //builder.setView(view);
        builder.show();
    }

    // 메모 변경 팝업
    @Subscribe
    public void MemoUpdateDialog(final Memo memo) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_input, null, false);
        final EditText memoEditText = (EditText) view.findViewById(R.id.text);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage("메모를 변경합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String title = memoEditText.getText().toString();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                memo.setMemo(title);

                realm.commitTransaction();
                realm.close();
            }
        });
        builder.setNegativeButton("취소", null);
        builder.setView(view);
        builder.show();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Memo memo = getItem(position);

        mMemoTextView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.setGravity(Gravity.RIGHT);
                popup.inflate(R.menu.memo_item);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                MemoUpdateDialog(memo);
                                break;
                            case R.id.delete:
                                MemoDeleteDialog(holder.getAdapterPosition());
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventBus.getDefault().post(memo.getId());
                return true;
            }
        });

        holder.memoTextView.setText(memo.getMemo());

        holder.checkBox.setChecked(memo.isDone());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                if (isChecked) {
                    memo.setDone(true);
                } else {
                    memo.setDone(false);
                }
                realm.commitTransaction();
                realm.close();
            }
        });
        if (memo.isDone()) {
            loadStrikeThroughSpan(holder, position);
        }
    }

    //삭제 선

    public void loadStrikeThroughSpan(final ViewHolder holder, final int position) {

        StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
        Memo memo = getItem(position);


        if (holder.checkBox.isChecked()) {
            holder.memoTextView.setText(memo.getMemo(), TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) holder.memoTextView.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, memo.getMemo().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView memoTextView;
        CheckBox checkBox;


        public ViewHolder(View itemView) {

            super(itemView);
            // 레이아웃 들고 오기
            TextView titleTextView = (TextView) itemView.findViewById(R.id.memo_textview);
            CheckBox imageView = (CheckBox) itemView.findViewById(R.id.checkBox);

            // 뷰 홀더에 넣는다
            this.memoTextView = titleTextView;
            this.checkBox = imageView;

        }
    }
}


