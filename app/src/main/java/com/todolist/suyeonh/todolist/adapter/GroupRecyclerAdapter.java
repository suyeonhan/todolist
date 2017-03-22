package com.todolist.suyeonh.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.todolist.suyeonh.todolist.MemocreateActivity;
import com.todolist.suyeonh.todolist.R;
import com.todolist.suyeonh.todolist.models.Group;

import org.greenrobot.eventbus.EventBus;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by junsuk on 2017. 3. 6..
 */

public class GroupRecyclerAdapter extends RealmRecyclerViewAdapter<Group, GroupRecyclerAdapter.ViewHolder> {

    private Context mContext;

    public GroupRecyclerAdapter(Context context, @Nullable OrderedRealmCollection<Group> data) {
        super(data, true);
        setHasStableIds(true);
        mContext = context;
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
        return new ViewHolder(convertView);
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
                Intent intent = new Intent(mContext, MemocreateActivity.class);
                intent.putExtra("id", group.getId());
                mContext.startActivity(intent);

//                ActivityCompat.startActivity(mContext,
//                        intent,
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
//                                Pair.create(holder.checkBox, "image")).toBundle());

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
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
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
