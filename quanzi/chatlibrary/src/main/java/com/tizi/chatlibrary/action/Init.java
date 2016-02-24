package com.tizi.chatlibrary.action;

import android.database.sqlite.SQLiteDatabase;

import com.tizi.chatlibrary.app.App;

/**
 * Created by qixingchen on 16/2/23.
 * 初始化library
 */
public class Init {

    //数据库
    public static SQLiteDatabase db;

    public static void init(String userID) {
        Init.db = new DataBaseHelper(App.applicationContext, userID, null, 2).getWritableDatabase();
    }
}
