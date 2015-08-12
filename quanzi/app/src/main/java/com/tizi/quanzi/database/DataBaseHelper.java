package com.tizi.quanzi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qixingchen on 15/8/12.
 * 本地数据库
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory,
                          int version) {
        super(context, name, cursorFactory, version);
    }

    public static class chatHistorySQLName {
        public static final String TableName = "chat_history";
        public static final String messID = "id";
        public static final String ConversationId = "group_id";
        public static final String uid = "uid";
        public static final String sender = "sender";
        public static final String create_time = "create_time";
        public static final String text = "text";
        public static final String type = "type";
        public static final String local_path = "local_path";
        public static final String url = "url";
        public static final String voice_duration = "voice_duration";
        public static final String isread = "isread";
        public static final String receiptTimestamp = "receiptTimestamp";
        public static final String status = "status";
        public static final String isSelfSend_ioType = "isSelfSend_ioType";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库后，对数据库的操作

        db.execSQL("create table singlechat_list(id varchar(40) primary key not null , uid varchar(40),uname varchar(40), text varchar(50), icon varchar(200),type Integer,create_user varchar(40), create_time integer, convid varchar(50), unread_count integer)");

        db.execSQL("create table chat_history(id varchar(40) PRIMARY KEY not null , group_id varchar(40),uid varchar(40), sender varchar(40),create_time integer, text varchar(1000), type INTEGER, local_path varchar(100), url varchar(200), voice_duration varchar(50), isread varchar(1),receiptTimestamp integer,status integer,isSelfSend_ioType char(1))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 更改数据库版本的操作
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //每次成功打开数据库后首先被执行
    }
}

