package com.todolist.suyeonh.todolist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todolist.suyeonh.todolist.R;

/**
 * Created by gkstn on 2017-03-07.
 */

public class Myadapter extends
        RecyclerView.Adapter<Myadapter.ViewHolder> {
    private String[] mDataSet;

    //생성자에서 데이터를 받음
    public Myadapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    //ViewHoler를 새로 만드는 부분으로
    // BaseAdapter의 getView() 중 currentView == null 일 경우에 해당
    // CursorAdapter의 newView()에 해당
    @Override
    public Myadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //아이템 레이아웃
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card,parent,false);

        //뷰홀더 생성 후 리턴
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //ViewHolder에 데이터를 설정하는 부분으로
    //BaseAdapter의 getView() 에서 데이터 설정하는 부분에 해당
    //CursorAdapter의 bindView()에 해당
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    { holder.textView.setText(mDataSet[position]);
    }

    //아이템의 갯수
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    //ViewHolder는 반드시 RecylerView.ViewHolder를 상속
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)
                    itemView.findViewById(R.id.card_text);
        }
    }
}
