package com.tizi.chatlibrary.action;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.model.message.CommentNotifyMessage;
import com.tizi.chatlibrary.model.message.SystemMessage;
import com.tizi.chatlibrary.staticSettings.StaticSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by qixingchen on 16/2/24.
 * 数据库的操作
 */
public class DatabaseAction {

    private static SQLiteDatabase db = Init.db;

    /*查询*/

    /**
     * 根据 ConversationId 查询所有聊天记录
     * 按照时间排序
     * 没有则返回空List
     *
     * @param ConversationId ConversationId
     *
     * @return ChatMessage 的 list
     *
     * @see ChatMessage
     */
    @NonNull
    public static List<ChatMessage> queryAllMessage(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.conversationId + "=?",//条件
                new String[]{ConversationId},//条件的参数
                null,//groupBy
                null,//having
                DataBaseHelper.chatHistorySQLName.createTime //+ " DESC"//orderBy
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
     * 根据 ConversationId 获取从start个开始向前的MessageLimit个消息
     * 按照时间排序
     * 没有则返回空List
     *
     * @param ConversationId ConversationId
     * @param start          开始数量
     *
     * @return ChatMessage 的 list
     *
     * @see ChatMessage
     */
    @NonNull
    public static List<ChatMessage> queryMessage(String ConversationId, int start) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.conversationId + "=?",//条件
                new String[]{ConversationId},//条件的参数
                null,//groupBy
                null,//having
                DataBaseHelper.chatHistorySQLName.createTime + " DESC",//orderBy
                String.format(Locale.getDefault(), "%d,%d", start, StaticSettings.Limit.MessageLimit)
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
     * 根据 messID 查询 ChatMessage
     *
     * @param messID 需查询的messID
     *
     * @return ChatMessage
     *
     * @see ChatMessage
     */
    @Nullable
    public static ChatMessage queryMessageByID(String messID) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
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
    public static ChatMessage queryOldestMessage(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.conversationId + "=?",//条件
                new String[]{ConversationId},//条件的参数
                null,//groupBy
                null,//having
                DataBaseHelper.chatHistorySQLName.createTime, //+ " DESC"//orderBy
                "1" //limit
        );
        chatMessageCursor.moveToFirst();
        if (chatMessageCursor.getCount() == 1) {
            ChatMessage chatMessage = chatMessageFromCursor(chatMessageCursor);
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
    public static ChatMessage queryNewestMessage(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.conversationId + "=?",//条件
                new String[]{ConversationId},//条件的参数
                null,//groupBy
                null,//having
                DataBaseHelper.chatHistorySQLName.createTime + " DESC",//orderBy
                "1" //limit
        );
        chatMessageCursor.moveToFirst();
        if (chatMessageCursor.getCount() == 1) {
            ChatMessage chatMessage = chatMessageFromCursor(chatMessageCursor);
            chatMessageCursor.close();
            return chatMessage;
        }
        chatMessageCursor.close();
        return null;
    }

    /**
     * 返回所查询的对话组的未读数量
     *
     * @param ConversationId 所需查询对话组的ConvID
     *
     * @return 未读数量
     */
    public static List<String> quaryUnreadList(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.isread + "=? and "
                        + DataBaseHelper.chatHistorySQLName.conversationId + "=?",//条件
                new String[]{"0", ConversationId},//条件的参数
                null,//groupBy
                null,//having
                null, //+ " DESC"//orderBy
                null //limit
        );
        chatMessageCursor.moveToFirst();
        ArrayList<String> ans = new ArrayList<>();
        while (!chatMessageCursor.isAfterLast()) {
            ans.add(chatMessageCursor.getString(chatMessageCursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.messID)));
            chatMessageCursor.moveToNext();
        }
        chatMessageCursor.close();
        return ans;
    }

    /**
     * 查询所有的图片消息
     */
    public static List<ChatMessage> quaryPhotoMess(String ConversationId) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.messageType + "=? and "
                        + DataBaseHelper.chatHistorySQLName.conversationId + "=?",//条件
                new String[]{String.valueOf(ChatMessage.MESSAGE_TYPE_IMAGE), ConversationId},//条件的参数
                null,//groupBy
                null,//having
                null, //+ " DESC"//orderBy
                null //limit
        );
        chatMessageCursor.moveToFirst();
        List<ChatMessage> ans = new ArrayList<>();
        while (!chatMessageCursor.isAfterLast()) {
            ChatMessage chatMessage = chatMessageFromCursor(chatMessageCursor);
            ans.add(chatMessage);

            chatMessageCursor.moveToNext();
        }
        chatMessageCursor.close();
        return ans;
    }

    /**
     * 获取所有系统消息
     *
     * @return 系统消息 List
     */
    public static List<SystemMessage> quaryAllSysMess() {
        Cursor sysMessCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.messageType + "=?",//条件
                new String[]{String.valueOf(ChatMessage.CONVERSATION_TYPE_SYSTEM)},//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        ArrayList<SystemMessage> systemMessageArrayList = new ArrayList<>();
        sysMessCursor.moveToFirst();
        while (!sysMessCursor.isAfterLast()) {
            SystemMessage temp = (SystemMessage) chatMessageFromCursor(sysMessCursor);
            if (temp != null) {
                systemMessageArrayList.add(temp);
            }
            sysMessCursor.moveToNext();
        }
        sysMessCursor.close();
        return systemMessageArrayList;
    }

    /**
     * 获取未读系统消息数量
     *
     * @return 系统消息 List
     */
    public static int quaryUnreadSysMess() {
        Cursor sysMessCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.messID},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.messageType + "=?and "
                        + DataBaseHelper.chatHistorySQLName.isread + "=?",
                new String[]{String.valueOf(ChatMessage.CONVERSATION_TYPE_SYSTEM), "N"},//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        int ans = sysMessCursor.getCount();
        sysMessCursor.close();
        return ans;
    }

    /**
     * 获取所有动态消息
     *
     * @return 动态消息 List
     */
    public static List<CommentNotifyMessage> quaryAllComment() {
        Cursor sysMessCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.Serializable},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.messageType + "=?",//条件
                new String[]{String.valueOf(ChatMessage.CONVERSATION_TYPE_DYN_COMMENT)},//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        ArrayList<CommentNotifyMessage> commentNotifyMessages = new ArrayList<>();
        sysMessCursor.moveToFirst();
        while (!sysMessCursor.isAfterLast()) {
            CommentNotifyMessage temp = (CommentNotifyMessage) chatMessageFromCursor(sysMessCursor);
            if (temp != null) {
                commentNotifyMessages.add(temp);
            }
            sysMessCursor.moveToNext();
        }
        sysMessCursor.close();
        return commentNotifyMessages;
    }

    /**
     * 获取未读动态消息数量
     *
     * @return 系统消息 List
     */
    public static int quaryUnreadComment() {
        Cursor sysMessCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                new String[]{DataBaseHelper.chatHistorySQLName.messID},//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.messageType + "=?and "
                        + DataBaseHelper.chatHistorySQLName.isread + "=?",
                new String[]{String.valueOf(ChatMessage.CONVERSATION_TYPE_DYN_COMMENT), "N"},//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        int ans = sysMessCursor.getCount();
        sysMessCursor.close();
        return ans;
    }

    /**
     * 根据 Cursor 转换 ChatMessage
     *
     * @param cursor 查询到的游标
     *
     * @return 返回的 ChatMessage
     */
    @Nullable
    private static ChatMessage chatMessageFromCursor(Cursor cursor) {
        ChatMessage chatMessage = null;
        byte[] SerializString = cursor.getBlob(
                cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.Serializable));
        Object object = SerializedObjectFormat.readSerializedObject(SerializString);
        if (object != null) {
            chatMessage = (ChatMessage) object;
        }
        return chatMessage;
    }

    /*chatGroup*/

    /**
     * 获取所有会话群
     *
     * @return List of ConvGroupAbs
     */
    @NonNull
    public static List<ConvGroupAbs> quaryAllChatGroup() {
        Cursor cursor = db.query(DataBaseHelper.chatGroupSQLNmae.TableName,//table name
                null,//返回的列,null表示全选
                null,//条件
                null,//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        List<ConvGroupAbs> groups = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ConvGroupAbs temp = ConvGroupAbsFromCursor(cursor);
            if (temp != null) {
                groups.add(temp);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return groups;
    }

    /**
     * 获得对应ID的会话组
     *
     * @param id 需要获得的对话组的ID
     *
     * @return 获得的会话组
     */
    @Nullable
    public static ConvGroupAbs quaryChatGroup(String id) {
        Cursor cursor = db.query(DataBaseHelper.chatGroupSQLNmae.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatGroupSQLNmae.id + "=?",//条件
                new String[]{id},//条件的参数
                null,//groupBy
                null,//having
                null //+ " DESC"//orderBy
        );
        ConvGroupAbs ans = null;
        cursor.moveToFirst();
        if (cursor.getCount() == 1) {
            ans = ConvGroupAbsFromCursor(cursor);
        }
        cursor.close();
        return ans;
    }

    /**
     * 根据 Cursor 转换 ConvGroupAbs
     *
     * @param cursor 查询到的游标
     *
     * @return 返回的 ConvGroupAbs
     */
    @Nullable
    private static ConvGroupAbs ConvGroupAbsFromCursor(Cursor cursor) {
        ConvGroupAbs convGroupAbs = null;
        byte[] SerializString = cursor.getBlob(
                cursor.getColumnIndex(DataBaseHelper.chatHistorySQLName.Serializable));
        Object object = SerializedObjectFormat.readSerializedObject(SerializString);
        if (object != null) {
            convGroupAbs = (ConvGroupAbs) object;
        }
        return convGroupAbs;
    }

    /*增加或更新*/

    /**
     * 添加或更新 chatMessage
     *
     * @param chatMessage 需要添加／更新的 ChatMessage {@link ChatMessage}
     */
    public static void addOrReplaceChatMessage(ChatMessage chatMessage) {
        ContentValues content = new ContentValues();

        content.put(DataBaseHelper.chatHistorySQLName.messID, chatMessage.getMessID());
        content.put(DataBaseHelper.chatHistorySQLName.conversationId, chatMessage.getConversationId());
        content.put(DataBaseHelper.chatHistorySQLName.conversationType, chatMessage.getConversationType());
        content.put(DataBaseHelper.chatHistorySQLName.createTime, chatMessage.getCreateTime());
        content.put(DataBaseHelper.chatHistorySQLName.messageType, chatMessage.getMessageType());
        content.put(DataBaseHelper.chatHistorySQLName.isread, chatMessage.isRead() ? "Y" : "N");
        content.put(DataBaseHelper.chatHistorySQLName.Serializable,
                SerializedObjectFormat.getSerializedObject(chatMessage));

        db.replace(DataBaseHelper.chatHistorySQLName.TableName, null, content);
    }

    /**
     * 将指定的会话全部标记为已读
     *
     * @param convID 需标记为已读的 ConvID
     */
    public static void setAllAsRead(String convID) {
        Cursor chatMessageCursor = db.query(DataBaseHelper.chatHistorySQLName.TableName,//table name
                null,//返回的列,null表示全选
                DataBaseHelper.chatHistorySQLName.isread + "=? and "
                        + DataBaseHelper.chatHistorySQLName.conversationId + "=?",//条件
                new String[]{"0", convID},//条件的参数
                null,//groupBy
                null,//having
                null, //+ " DESC"//orderBy
                null //limit
        );
        chatMessageCursor.moveToFirst();
        while (!chatMessageCursor.isAfterLast()) {
            ChatMessage chatMessage = chatMessageFromCursor(chatMessageCursor);
            if (chatMessage != null) {
                chatMessage.setRead(true);
                addOrReplaceChatMessage(chatMessage);
            }
            chatMessageCursor.moveToNext();
        }
        chatMessageCursor.close();
    }

    /**
     * 添加或更新 ChatMessPair
     */
    public static void addOrReplaceChatGroup(ConvGroupAbs group) {

        ContentValues content = new ContentValues();
        content.put(DataBaseHelper.chatGroupSQLNmae.id, group.getID());
        content.put(DataBaseHelper.chatGroupSQLNmae.conversationType, group.getType());
        content.put(DataBaseHelper.chatGroupSQLNmae.Serializable,
                SerializedObjectFormat.getSerializedObject(group));
        db.replace(DataBaseHelper.chatGroupSQLNmae.TableName, null, content);
    }

    /*删除*/

    /**
     * 删除 convID 对应的所有聊天消息
     *
     * @param convID 要删除的 convID
     */
    public static void deleteAllMessage(String convID) {
        db.delete(DataBaseHelper.chatHistorySQLName.TableName, DataBaseHelper.chatHistorySQLName.conversationId + "=?",
                new String[]{convID});
    }

    /**
     * 删除 MessageID 对应的聊天消息
     *
     * @param MessID 要删除的 MessID
     */
    public static void deleteMessage(String MessID) {
        db.delete(DataBaseHelper.chatHistorySQLName.TableName, DataBaseHelper.chatHistorySQLName.messID + "=?",
                new String[]{MessID});
    }

    /**
     * 删除所有的系统消息
     */
    public static void deleteAllSystemMessage() {
        db.delete(DataBaseHelper.chatHistorySQLName.TableName, DataBaseHelper.chatHistorySQLName.conversationType + "=?",
                new String[]{String.valueOf(ChatMessage.CONVERSATION_TYPE_SYSTEM)});
    }

    /**
     * 删除聊天组
     *
     * @param ID 要删除的组的ID
     */
    public static void deleteGroup(String ID) {
        db.delete(DataBaseHelper.chatGroupSQLNmae.TableName, DataBaseHelper.chatGroupSQLNmae.id + "=?",
                new String[]{ID});
    }

}
