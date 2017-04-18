package com.todolist.suyeonh.todolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.todolist.suyeonh.todolist.R;
import com.todolist.suyeonh.todolist.models.Group;

import java.util.List;

/**
 * Created by gkstn on 2017-03-09.
 */

public class MemoAdapter extends BaseAdapter {
    private List<Group> mData;

    public void swap(List<Group> newGroupList) {
        mData = newGroupList;
        //notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        //컨버트 뷰= 재사용 뷰
        if (convertView == null) {
            viewHolder = new ViewHolder();

            //뷰를 새로 만들 때
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group, parent, false);

            //레이아웃 들고 오기
            TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text);

            //뷰 홀더에 넣는다
            viewHolder.titleTextView = titleTextView;

            convertView.setTag(viewHolder);
        } else {
            // 재사용 할 때
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //데이터
        Group group = mData.get(position);

        //화면에 뿌리기
        viewHolder.titleTextView.setText(group.getTitle());

        return convertView;
    }
        private static class ViewHolder {
            TextView titleTextView;
        }
    }
