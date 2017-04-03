package com.todolist.suyeonh.todolist.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.todolist.suyeonh.todolist.MemoActivity;
import com.todolist.suyeonh.todolist.R;
import com.todolist.suyeonh.todolist.models.Group;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by junsuk on 2017. 3. 6..
 */

public class GroupRecyclerAdapter extends RealmRecyclerViewAdapter<Group, GroupRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private Button mOptionView;
    private Realm mRealm;


    public GroupRecyclerAdapter(Context context, @Nullable OrderedRealmCollection<Group> data) {
        super(data, true);
        setHasStableIds(true);
        mContext = context;

    }

    public void delete(int adapterPosition) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Group group = getItem(adapterPosition);
        group.deleteFromRealm();
        notifyItemRemoved(adapterPosition);
        realm.commitTransaction();
        realm.close();
    }

    // EventBus 용 이벤트
    public static class ItemClickEvent {
        public ItemClickEvent(Group group) {
            this.group = group;
        }

        public Group group;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 뷰를 새로 만들 때

        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group, parent, false);
        mOptionView = (Button) convertView.findViewById(R.id.option_button);
        return new ViewHolder(convertView);
    }


    // 그룹 삭제 팝업
    @Subscribe
    public void GroupDeleteDialog(final Long id) {
        //View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("그룹을 삭제합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mRealm = Realm.getDefaultInstance();
                mRealm.beginTransaction();

                mRealm.where(Group.class).equalTo("id", id).findFirst().deleteFromRealm();

                mRealm.commitTransaction();
                mRealm.close();

            }
        });
        builder.setNegativeButton("취소", null);
        //builder.setView(view);
        builder.show();
    }

    // 그룹명 변경 팝업
    @Subscribe
    public void GroupUpdateDialog(final Long id) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_input, null, false);
        final EditText groupEditText = (EditText) view.findViewById(R.id.text);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage("그룹명을 변경합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String title = groupEditText.getText().toString();

                mRealm = Realm.getDefaultInstance();
                mRealm.beginTransaction();

                Group group = mRealm.where(Group.class).equalTo("id", id).findFirst();
                group.setTitle(title);

                mRealm.commitTransaction();
                mRealm.close();
            }
        });
        builder.setNegativeButton("취소", null);
        builder.setView(view);
        builder.show();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 데이터
        final Group group = getItem(position);

        // 화면에 뿌리기
        holder.titleTextView.setText(group.getTitle());

        if (group.getImagePath() != null) {
            Glide.with(mContext).load(group.getImagePath()).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MemoActivity.class);
                intent.putExtra("id", group.getId());
                mContext.startActivity(intent);

//                ActivityCompat.startActivity(mContext,
//                        intent,
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
//                                Pair.create(holder.checkBox, "image")).toBundle());

            }
        });


        mOptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);

                popup.inflate(R.menu.group_item);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                GroupUpdateDialog(group.getId());
                                break;
                            case R.id.delete:
                                GroupDeleteDialog(group.getId());
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventBus.getDefault().post(group.getId());
                return true;
            }
        });
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

//    public void swap(List<Group> memoList) {
//        mData = memoList;
//        notifyDataSetChanged();
//    }
//
//    public void insert(List<Group> memoList) {
//        mData = memoList;
//        notifyItemInserted(0);
//    }
//
//    public void update(List<Group> memoList, int position) {
//        mData = memoList;
//        notifyItemChanged(position);
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            // 레이아웃 들고 오기
            TextView titleTextView = (TextView) itemView.findViewById(R.id.title_text);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.image_view);

            // 뷰 홀더에 넣는다
            this.titleTextView = titleTextView;
            this.imageView = imageView;
        }
    }
}
