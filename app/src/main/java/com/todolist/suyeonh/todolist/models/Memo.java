package com.todolist.suyeonh.todolist.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gkstn on 2017-03-09.
 */

public class Memo extends RealmObject implements Parcelable {

    @PrimaryKey
    private long id;
    private String title;
    private String content;
    private String imagePath;

    public Memo() {
    }

    public Memo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Memo{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", imagePath='").append(imagePath).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.imagePath);
    }

    protected Memo(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        this.imagePath = in.readString();
    }

    public static long nextId(Realm realm) {
        Number currentIdNum = realm.where(Memo.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public static final Parcelable.Creator<Memo> CREATOR = new Parcelable.Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel source) {
            return new Memo(source);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };
}
