package com.tizi.quanzi.database;

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
        db.execSQL("create table chat_history(id varchar(40)PRIMARY KEY not null, group_id varchar(40),convID varchar(50), account varchar(40), sender varchar(40), send_time integer, text varchar(1000), type INTEGER, local_path varchar(100), url varchar(200), voice_duration varchar(50), isread varchar(1),receiptTimestamp integer,status integer,isSelfSend_ioType char(1),userName char(40),conv_type integer,imageHeight integer,imageWeight integer,chatImage varchar(200),chat_from integer)");

        /*系统消息记录*/
        db.execSQL("create table systemMess( id varchar(40) PRIMARY KEY not null ,create_time INTEGER, isread varchar(1) ,Serializable blob )");

        /*私聊群*/
        db.execSQL("create table privateMessGroup( id varchar(40) PRIMARY KEY not null,Serializable blob )");

        /*聊天组*/
        db.execSQL("create table ChatGroup( id varchar(40) PRIMARY KEY not null,type integer,Serializable blob )");
    }

    /* 更改数据库版本的操作*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL("create table ChatGroup( id varchar(40) PRIMARY KEY not null,type integer,Serializable blob )");
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
        public static final String messID = "id";
        public static final String ConversationId = "convID";
        public static final String Group_id = "group_id";
        public static final String uid = "account";
        public static final String sender = "sender";
        public static final String send_time = "send_time";
        public static final String text = "text";
        public static final String type = "type";
        public static final String local_path = "local_path";
        public static final String url = "url";
        public static final String voice_duration = "voice_duration";
        public static final String isread = "isread";
        public static final String receiptTimestamp = "receiptTimestamp";
        public static final String status = "status";
        public static final String isSelfSend_ioType = "isSelfSend_ioType";
        public static final String userName = "userName";
        public static final String conv_type = "conv_type";
        public static final String imageHeight = "imageHeight";
        public static final String imageWeight = "imageWeight";
        public static final String chatImage = "chatImage";
        public static final String from = "chat_from";

    }

    /**
     * SystemMess 表列名
     */
    public static class SystemMessSQLName {
        public static final String TableName = "systemMess";
        public static final String id = "id";
        public static final String create_time = "create_time";
        public static final String isread = "isread";
        public static final String Serializable = "Serializable";
    }

    /**
     * privateMessGroup 表名
     */
    public static class privateMessGroupSQLNmae {
        public static final String TableName = "privateMessGroup";
        public static final String id = "id";
        public static final String Serializable = "Serializable";
    }

    /**
     * ChatGroup 表名
     */
    public static class chatGroupSQLNmae {
        public static final String TableName = "ChatGroup";
        public static final String id = "id";
        public static final String type = "type";
        public static final String Serializable = "Serializable";
    }
}

