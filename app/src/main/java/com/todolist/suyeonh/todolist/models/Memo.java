package com.todolist.suyeonh.todolist.models;

import io.realm.RealmObject;

/**
 * Created by gkstn on 2017-03-20.
 */

public class Memo extends RealmObject {
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
