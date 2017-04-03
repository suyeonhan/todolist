package com.todolist.suyeonh.todolist.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by gkstn on 2017-03-20.
 */

public class Memo extends RealmObject implements Parcelable {

    private String memo;
    private boolean isDone = false;
    private long id;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) { this.memo = memo; }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memo);
        dest.writeByte(this.isDone ? (byte) 1 : (byte) 0);
        dest.writeLong(this.id);
    }

    public Memo() {
    }

    protected Memo(Parcel in) {
        this.memo = in.readString();
        this.isDone = in.readByte() != 0;
        this.id = in.readLong();
    }

    public static final Creator<Memo> CREATOR = new Creator<Memo>() {
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


