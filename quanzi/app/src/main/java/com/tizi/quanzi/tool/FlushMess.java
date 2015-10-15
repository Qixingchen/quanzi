package com.tizi.quanzi.tool;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.ChatMessFormatFromAVIM;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.SystemMessage;

import java.util.List;

/**
 * Created by qixingchen on 15/9/1.
 * 初始化的更新消息
 */
public class FlushMess {
    private static final String TAG = FlushMess.class.getSimpleName();
    private final int QueryLimit = StaticField.QueryLimit.MessageLimit;
    private AVIMMessagesQueryCallback avimMessagesQueryCallback;
    private AVIMConversation conversation;

    public static FlushMess getInstance() {
        return new FlushMess();
    }

    /**
     * 刷新聊天记录，从LC
     *
     * @param convID 需刷新的convID
     */
    public void Flush(final String convID) {

        final int[] lastFlushNum = {0};
        conversation = AppStaticValue.getImClient().getConversation(convID);
        final int[] flushTimes = {0};

        //聊天消息获取回调
        avimMessagesQueryCallback = new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                lastFlushNum[0] = list.size();
                if (lastFlushNum[0] == 0) {
                    return;
                }
                for (AVIMMessage avimMessage : list) {
                    try {
                        ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage((AVIMTypedMessage) avimMessage);
                        DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
                        if (flushTimes[0] == 0) {
                            GroupList.getInstance().updateGroupLastMess(convID, ChatMessage.getContentText(chatMessage),
                                    chatMessage.create_time);
                        }
                    } catch (ClassFormatError formatError) {
                        SystemMessage systemMessage = ChatMessFormatFromAVIM.SysMessFromAVMess((AVIMTypedMessage) avimMessage);
                        MutiTypeMsgHandler.HandlerSystemMess(systemMessage, true);
                    }

                }
                if (flushTimes[0] < StaticField.QueryLimit.FlushMaxTimes &&
                        lastFlushNum[0] == StaticField.QueryLimit.MessageLimit) {
                    flushTimes[0]++;
                    doFlush(convID);
                }
            }
        };

        doFlush(convID);
    }

    private void doFlush(String convID) {
        ChatMessage oldestChatMess = DBAct.getInstance().queryOldestMessage(convID);
        if (oldestChatMess != null) {
            conversation.queryMessages(oldestChatMess.messID, oldestChatMess.create_time,
                    QueryLimit, avimMessagesQueryCallback);
        } else {
            conversation.queryMessages(QueryLimit, avimMessagesQueryCallback);
        }
    }
}
