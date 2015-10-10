package com.tizi.quanzi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/8/12.
 * 数据库操作汇总
 * todo 使用workThread操作
 * todo 事务回滚  https://groups.google.com/forum/#!topic/android-developers/dsEr5hj8k90
 */
public class DBAct {
    private static final String TAG = "数据库操作";
    private static DBAct mInstance;
    private SQLiteDatabase db;

    private DBAct() {
        db = AppStaticValue.getDatabase();
    }

    public static DBAct getInstance() {
        if (mInstance == null) {
            synchronized (DBAct.class) {
                if (mInstance == null) {
                    mInstance = new DBAct();
                }
            }
        }
        return mInstance;
    }

    /*查询*/

    /*ChatMessage*/

    /**
     * 根据 ConversationId 查询所有聊天记录
     * 按照时间排序
     * 没有则返回空List
     *
     * @param ConversationId ConversationId
     *
     * @return ChatMessage 的 list
     *
     * @see com.avos.avoscloud.im.v2.AVIMConversation
     * @see ChatMessage
     */
    @NonNull
    public List<ChatMessage> queryMessage(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.ConversationId + "=?",//条件
                new String[]{ConversationId},//条件的参数
                null,//groupBy
                null,//having
                DataBaseHelper.chatHistorySQLName.send_time //+ " DESC"//orderBy
        );

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessageCursor.moveToFirst();
        while (!chatMessageCursor.isAfterLast()) {
            chatMessages.add(chatMessageFromCursor(chatMessageCursor));
            chatMessageCursor.moveToNext();
        }
        chatMessageCursor.close();
        return chatMessages;

    }

    /**
     * 根据 Cursor 转换 ChatMessage
     *
     * @param cursor 查询到的游标
     *
     * @return 返回的 ChatMessage
     */
    private ChatMessage chatMessageFromCursor(Cursor cursor) {
        ChatMessage chatMessage = new ChatMessage();

        //int
        chatMessage.type = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.type));
        chatMessage.status = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.status));
        //from需要在isSelfSend后判断
        chatMessage.ChatBothUserType = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.conv_type));
        chatMessage.imageHeight = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.imageHeight));
        chatMessage.imageWeight = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.imageWeight));

        //string
        chatMessage.text = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.text));
        chatMessage.ConversationId = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.ConversationId));
        chatMessage.uid = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.uid));
        chatMessage.sender = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.sender));
        chatMessage.local_path = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.local_path));
        chatMessage.url = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.url));
        chatMessage.messID = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.messID));
        chatMessage.chatImage = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.chatImage));
        chatMessage.userName = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.userName));
        chatMessage.groupID = cursor.getString(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.Group_id));


        //boolean
        chatMessage.isSelfSend = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.isSelfSend_ioType)) > 0;
        chatMessage.isread = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.isread)) > 0;


        //long
        chatMessage.create_time = cursor.getLong(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.send_time));

        chatMessage.receiptTimestamp = cursor.getLong(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.receiptTimestamp));

        //double
        chatMessage.voice_duration = cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.voice_duration));

        //etc
        if (chatMessage.isSelfSend) {
            chatMessage.From = StaticField.ChatFrom.ME;
        }//todo 判断是本群还是临时群
        else {
            if (chatMessage.ChatBothUserType == StaticField.ConvType.GROUP) {
                chatMessage.From = StaticField.ChatFrom.OTHER;
            } else {
                chatMessage.From = StaticField.ChatFrom.OTHER;
            }
        }


        return chatMessage;
    }

    /**
     * 根据 messID 查询 ChatMessage
     *
     * @param messID 需查询的messID
     *
     * @return ChatMessage
     *
     * @see ChatMessage
     */
    @Nullable
    public ChatMessage queryMessageByID(String messID) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.messID + "=?",//条件
                new String[]{messID},//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        chatMessageCursor.moveToFirst();
        ChatMessage chatMessage = null;
        if (chatMessageCursor.getCount() == 1) {
            chatMessage = chatMessageFromCursor(chatMessageCursor);
        }
        chatMessageCursor.close();
        return chatMessage;
    }

    /**
     * 查询 ConversationId 在数据库中最旧的 ChatMessage
     *
     * @param ConversationId ConversationId
     *
     * @return ChatMessage
     *
     * @see ChatMessage
     */
    @Nullable
    public ChatMessage queryOldestMessage(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.ConversationId + "=?",//条件
                new String[]{ConversationId},//条件的参数
                null,//groupBy
                null,//having
                DataBaseHelper.chatHistorySQLName.send_time, //+ " DESC"//orderBy
                "1" //limit
        );
        chatMessageCursor.moveToFirst();
        if (chatMessageCursor.getCount() == 1) {
            ChatMessage chatMessage = chatMessageFromCursor(chatMessageCursor);
            Log.w(TAG, "最旧的消息是：" + chatMessage.toString());
            chatMessageCursor.close();
            return chatMessage;
        }
        chatMessageCursor.close();
        return null;
    }

    /**
     * 查询 ConversationId 在数据库中最新的 ChatMessage
     *
     * @param ConversationId ConversationId
     *
     * @return ChatMessage
     *
     * @see ChatMessage
     */
    @Nullable
    public ChatMessage queryNewestMessage(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.ConversationId + "=?",//条件
                new String[]{ConversationId},//条件的参数
                null,//groupBy
                null,//having
                DataBaseHelper.chatHistorySQLName.send_time + " DESC",//orderBy
                "1" //limit
        );
        chatMessageCursor.moveToFirst();
        if (chatMessageCursor.getCount() == 1) {
            ChatMessage chatMessage = chatMessageFromCursor(chatMessageCursor);
            Log.w(TAG, "最旧的消息是：" + chatMessage.toString());
            chatMessageCursor.close();
            return chatMessage;
        }
        chatMessageCursor.close();
        return null;
    }

    /**
     * 将数据库中的所有私聊信息加入PrivateList {@link com.tizi.quanzi.dataStatic.PrivateMessPairList}
     */
    public void addPrivateMessToList() {
        Cursor privateMessCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,
                null,
                DataBaseHelper.chatHistorySQLName.conv_type + "=?",
                new String[]{String.valueOf(StaticField.ConvType.twoPerson)},
                null,
                null,
                DataBaseHelper.chatHistorySQLName.send_time + " DESC",//orderBy
                "1" //limit
        );

        privateMessCursor.moveToFirst();
        while (!privateMessCursor.isAfterLast()) {
            ChatMessage chatMessage = chatMessageFromCursor(privateMessCursor);
            if (PrivateMessPairList.getInstance().getGroup(chatMessage.groupID) == null) {
                PrivateMessPairList.getInstance().addGroup(PrivateMessPair.newPrivatePair(chatMessage));
            } else {
                PrivateMessPairList.getInstance().updateGroupLastMess(chatMessage.ConversationId,
                        ChatMessage.getContentText(chatMessage), chatMessage.create_time);
            }
            privateMessCursor.moveToNext();
        }
        privateMessCursor.close();
    }

    /**
     * 返回所查询的对话组的未读数量
     *
     * @param ConversationId 所需查询对话组的ConvID
     *
     * @return 未读数量
     */
    public int quaryUnreadCount(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.isread + "=? and "
                        + DataBaseHelper.chatHistorySQLName.ConversationId + "=?",//条件
                new String[]{"0", ConversationId},//条件的参数
                null,//groupBy
                null,//having
                null, //+ " DESC"//orderBy
                null //limit
        );
        chatMessageCursor.moveToFirst();
        int count = chatMessageCursor.getCount();
        chatMessageCursor.close();
        return count;
    }

    /*SystemMessage*/

    /**
     * 获取指定的系统消息
     *
     * @return 系统消息 {@link SystemMessage}
     */
    public SystemMessage quarySysMess(String messID) {
        Cursor sysMessCursor = db.query(DataBaseHelper.SystemMessSQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.SystemMessSQLName.id + "=?",//条件
                new String[]{messID},//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        SystemMessage systemMessage = new SystemMessage();
        sysMessCursor.moveToFirst();
        if (sysMessCursor.getCount() == 1) {
            systemMessage = sysMessFromCursor(sysMessCursor);
        }
        sysMessCursor.close();
        return systemMessage;
    }

    /**
     * 获取系统消息
     *
     * @return 系统消息 List {@link SystemMessage}
     */
    public List<SystemMessage> quaryAllSysMess() {
        Cursor sysMessCursor = db.query(DataBaseHelper.SystemMessSQLName.TableName,//table name
                null,//返回的列,null表示全选
                null,//条件
                null,//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        ArrayList<SystemMessage> systemMessageArrayList = new ArrayList<>();
        sysMessCursor.moveToFirst();
        while (!sysMessCursor.isAfterLast()) {
            SystemMessage temp = sysMessFromCursor(sysMessCursor);
            if (temp != null) {
                systemMessageArrayList.add(temp);
            }
            sysMessCursor.moveToNext();
        }
        sysMessCursor.close();
        return systemMessageArrayList;
    }


    /**
     * 根据 Cursor 转换 SystemMessage
     *
     * @param cursor 查询到的游标
     *
     * @return 返回的 SystemMessage
     */
    @Nullable
    private SystemMessage sysMessFromCursor(Cursor cursor) {
        SystemMessage systemMessage = new SystemMessage();
        byte[] SerializString = cursor.getBlob(
                cursor.getColumnIndex(DataBaseHelper.SystemMessSQLName.Serializable));
        Object object = SerializedObjectFormat.readSerializedObject(SerializString);
        if (object != null) {
            systemMessage = (SystemMessage) object;
        }
        return systemMessage;
    }

    /*判断*/


    /*增加或更新*/

    /**
     * 添加或更新 chatMessage
     * 如果 chatMessage.isread 则更新这个项目，否则不更新
     *
     * @param chatMessage 需要添加／更新的 ChatMessage {@link ChatMessage}
     */
    public void addOrReplaceChatMessage(ChatMessage chatMessage) {

        ContentValues content = new ContentValues();
        ChatMessage oldChatMess = queryMessageByID(chatMessage.messID);
        if (oldChatMess != null) {
            if (chatMessage.isread) {
                content.put(DataBaseHelper.chatHistorySQLName.isread, true);
            } else {
                content.put(DataBaseHelper.chatHistorySQLName.isread, oldChatMess.isread);
            }
        } else {
            content.put(DataBaseHelper.chatHistorySQLName.isread, chatMessage.isread);
        }
        content.put(DataBaseHelper.chatHistorySQLName.messID, chatMessage.messID);
        content.put(DataBaseHelper.chatHistorySQLName.ConversationId, chatMessage.ConversationId);
        content.put(DataBaseHelper.chatHistorySQLName.uid, chatMessage.uid);
        content.put(DataBaseHelper.chatHistorySQLName.sender, chatMessage.sender);
        content.put(DataBaseHelper.chatHistorySQLName.send_time, chatMessage.create_time);
        content.put(DataBaseHelper.chatHistorySQLName.text, chatMessage.text);
        content.put(DataBaseHelper.chatHistorySQLName.type, chatMessage.type);
        content.put(DataBaseHelper.chatHistorySQLName.local_path, chatMessage.local_path);
        content.put(DataBaseHelper.chatHistorySQLName.url, chatMessage.url);
        content.put(DataBaseHelper.chatHistorySQLName.voice_duration, chatMessage.voice_duration);

        content.put(DataBaseHelper.chatHistorySQLName.status, chatMessage.status);
        content.put(DataBaseHelper.chatHistorySQLName.receiptTimestamp, chatMessage.receiptTimestamp);
        content.put(DataBaseHelper.chatHistorySQLName.isSelfSend_ioType, chatMessage.isSelfSend);
        content.put(DataBaseHelper.chatHistorySQLName.userName, chatMessage.userName);
        content.put(DataBaseHelper.chatHistorySQLName.Group_id, chatMessage.groupID);
        content.put(DataBaseHelper.chatHistorySQLName.imageHeight, chatMessage.imageHeight);
        content.put(DataBaseHelper.chatHistorySQLName.imageWeight, chatMessage.imageWeight);
        content.put(DataBaseHelper.chatHistorySQLName.conv_type, chatMessage.ChatBothUserType);
        content.put(DataBaseHelper.chatHistorySQLName.chatImage, chatMessage.chatImage);


        db.replace(DataBaseHelper.chatHistorySQLName.TableName, null, content);
    }

    /**
     * 新增系统消息
     *
     * @param systemMessage 需要增加或修改的系统消息 {@link SystemMessage}
     */
    public void addOrReplaceSysMess(SystemMessage systemMessage) {
        ContentValues content = new ContentValues();
        content.put(DataBaseHelper.SystemMessSQLName.id, systemMessage.getId());
        content.put(DataBaseHelper.SystemMessSQLName.create_time, systemMessage.getCreate_time());
        content.put(DataBaseHelper.SystemMessSQLName.isread, systemMessage.isread());
        content.put(DataBaseHelper.SystemMessSQLName.Serializable,
                SerializedObjectFormat.getSerializedObject(systemMessage));
        db.replace(DataBaseHelper.SystemMessSQLName.TableName, null, content);
    }

    /**
     * 全部标记为已读
     *
     * @param convID 需标记为已读的 ConvID
     */
    public void setAllAsRead(String convID) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.isread + "=? and "
                        + DataBaseHelper.chatHistorySQLName.ConversationId + "=?",//条件
                new String[]{"0", convID},//条件的参数
                null,//groupBy
                null,//having
                null, //+ " DESC"//orderBy
                null //limit
        );
        chatMessageCursor.moveToFirst();
        while (!chatMessageCursor.isAfterLast()) {
            ChatMessage chatMessage = chatMessageFromCursor(chatMessageCursor);
            chatMessage.isread = true;
            addOrReplaceChatMessage(chatMessage);
            chatMessageCursor.moveToNext();
        }
        chatMessageCursor.close();
    }


    /*删除*/

    /**
     * 删除 convID 对应的所有聊天消息
     *
     * @param convID 要删除的 convID
     */
    public void deleteAllMessage(String convID) {
        db.delete(DataBaseHelper.chatHistorySQLName.TableName, DataBaseHelper.chatHistorySQLName.ConversationId + "=?",
                new String[]{convID});
    }

    /**
     * 删除 MessageID 对应的聊天消息
     *
     * @param MessID 要删除的 MessID
     */
    public void deleteMessage(String MessID) {
        db.delete(DataBaseHelper.chatHistorySQLName.TableName, DataBaseHelper.chatHistorySQLName.messID + "=?",
                new String[]{MessID});
    }
}
