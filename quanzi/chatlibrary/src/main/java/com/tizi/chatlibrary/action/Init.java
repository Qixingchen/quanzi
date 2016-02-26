package com.tizi.chatlibrary.action;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.tizi.chatlibrary.model.message.ConvertMessage;

/**
 * Created by qixingchen on 16/2/23.
 * 初始化library
 */
public class Init {

    //数据库
    public static SQLiteDatabase db;

    public static Application application;

    /**
     * 初始化library
     *
     * @param userID      用户ID,用于建立分用户的数据库
     * @param application context
     */
    public static void init(String userID, Application application) {
        Init.application = application;
        Init.db = new DataBaseHelper(application, "chat" + userID, null, 2).getWritableDatabase();
    }

    /**
     * 设置消息转换接口
     */
    public static void setConvert(ConvertMessage.Convert convert) {
        ConvertMessage.getInstance().setConvert(convert);
    }
}
