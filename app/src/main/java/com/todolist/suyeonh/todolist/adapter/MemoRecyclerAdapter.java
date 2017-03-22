package com.todolist.suyeonh.todolist.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.todolist.suyeonh.todolist.R;
import com.todolist.suyeonh.todolist.models.Memo;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by gkstn on 2017-03-20.
 */

public class MemoRecyclerAdapter extends RealmRecyclerViewAdapter<Memo, MemoRecyclerAdapter.ViewHolder> {

    public MemoRecyclerAdapter(@Nullable OrderedRealmCollection<Memo> data) {
        super(data, true);
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memo, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Memo memo = getItem(position);
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
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView memoTextView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {

            super(itemView);

            // 레이아웃 들고 오기
            TextView titleTextView = (TextView) itemView.findViewById(R.id.textview);
            CheckBox imageView = (CheckBox) itemView.findViewById(R.id.checkBox);

            // 뷰 홀더에 넣는다
            this.memoTextView = titleTextView;
            this.checkBox = imageView;
        }
    }
}
