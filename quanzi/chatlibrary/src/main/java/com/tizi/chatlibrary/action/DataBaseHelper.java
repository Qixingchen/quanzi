package com.tizi.chatlibrary.action;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qixingchen on 15/8/12.
 * 本地数据库
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    /* 创建数据库后，对数据库的操作*/
    @Override
    public void onCreate(SQLiteDatabase db) {

        /*聊天记录*/
        db.execSQL("create table chat_history(messID varchar(40)PRIMARY KEY not null,createTime integer, conversationId varchar(50),messageType integer,conversationType integer,isread varchar(1),Serializable blob");

        /*聊天组*/
        db.execSQL("create table ChatGroup( id varchar(40) PRIMARY KEY not null,conversationType integer,Serializable blob )");
    }

    /* 更改数据库版本的操作*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:

                //do not add break
            default:

        }
    }

    /*每次成功打开数据库后首先被执行*/
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }

    /**
     * chat_history数据库列名
     */
    public static class chatHistorySQLName {
        public static final String TableName = "chat_history";
        public static final String messID = "messID";
        public static final String conversationId = "conversationId";
        public static final String conversationType = "conversationType";
        public static final String createTime = "createTime";
        public static final String messageType = "messageType";
        public static final String isread = "isread";
        public static final String Serializable = "Serializable";
    }

    /**
     * ChatGroup 表名
     */
    public static class chatGroupSQLNmae {
        public static final String TableName = "ChatGroup";
        public static final String id = "id";
        public static final String conversationType = "conversationType";
        public static final String Serializable = "Serializable";
    }
}

