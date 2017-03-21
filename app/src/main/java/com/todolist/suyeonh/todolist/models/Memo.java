package com.todolist.suyeonh.todolist.models;

import io.realm.RealmObject;

/**
 * Created by gkstn on 2017-03-20.
 */

public class Memo extends RealmObject {
    private String memo;
    private boolean isDone = false;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
