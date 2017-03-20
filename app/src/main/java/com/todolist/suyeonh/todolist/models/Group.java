package com.todolist.suyeonh.todolist.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gkstn on 2017-03-09.
 */

public class Group extends RealmObject implements Parcelable {

    @PrimaryKey
    private long id;
    private String title;
    private String imagePath;
    private int progress = 0;

    private RealmList<Memo> memoList;

    public Group() {
    }

    public Group(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public RealmList<Memo> getMemoList() {
        return memoList;
    }

    public void setMemoList(RealmList<Memo> memoList) {
        this.memoList = memoList;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", progress=" + progress +
                '}';
    }


    public static long nextId(Realm realm) {
        Number currentIdNum = realm.where(Group.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.imagePath);
        dest.writeInt(this.progress);
    }

    protected Group(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.imagePath = in.readString();
        this.progress = in.readInt();
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
