package com.todolist.suyeonh.todolist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by gkstn on 2017-03-15.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //램 초기화
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
