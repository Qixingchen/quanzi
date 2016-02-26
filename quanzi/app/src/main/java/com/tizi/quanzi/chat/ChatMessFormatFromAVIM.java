package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMFileMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.model.message.CommentNotifyMessage;
import com.tizi.chatlibrary.model.message.ImageChatMessage;
import com.tizi.chatlibrary.model.message.SystemMessage;
import com.tizi.chatlibrary.model.message.VoiceChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/8/14.
 * 将AVIM消息类型转换为ChatMessage
 */
public class ChatMessFormatFromAVIM {

    private static final String TAG = ChatMessFormatFromAVIM.class.getSimpleName();

    /**
     * 将 AVIMTypedMessage 转换为 ChatMessage
     *
     * @param message 需要转换的 AVIMTypedMessage
     *
     * @return ChatMessage {@link ChatMessage}
     */

    public static ChatMessage ChatMessageFromAVMessage(AVIMTypedMessage message) throws ClassFormatError {
        ChatMessage chatMessage;
        if (message.getMessageType() == AVIMReservedMessageType.TextMessageType.getType()) {
            /*判断是否为系统消息*/
            String type = (String) ((AVIMTextMessage) message).getAttrs().get(
                    StaticField.ChatMessAttrName.IS_SYS_MESS);

            if (type.compareTo(StaticField.SystemMessAttrName.MessTypeCode.System_mess) == 0) {
                int systemMesstype = ((int) ((AVIMTextMessage) message).getAttrs().get(
                        StaticField.SystemMessAttrName.SYS_MSG_FLAG));
                if (systemMesstype == com.tizi.chatlibrary.model.message.SystemMessage.dyn_comment) {
                    chatMessage = dynCommentMessFromAVMessage((AVIMTextMessage) message);
                    Log.i(TAG, "动态消息");
                } else {
                    chatMessage = SysMessFromAVMess((AVIMTextMessage) message);
                    Log.i(TAG, "系统消息");
                }

            } else {
                chatMessage = textChatMessageFromAVMessage(message);
                Log.i(TAG, "文本消息");
            }
        } else if (message.getMessageType() == AVIMReservedMessageType.ImageMessageType.getType()) {
            chatMessage = imageChatMessageFromAVMessage(message);
            Log.i(TAG, "图片消息。");
        } else if (message.getMessageType() == AVIMReservedMessageType.AudioMessageType.getType()) {
            chatMessage = voiceChatMessageFromAVMessage(message);
            Log.i(TAG, "音频消息。");
        } else if (message.getMessageType() == AVIMReservedMessageType.VideoMessageType.getType()) {
            chatMessage = vedioChatMessageFromAVMessage(message);
            Log.i(TAG, "视频消息");
        } else if (message.getMessageType() == AVIMReservedMessageType.LocationMessageType.getType()) {
            chatMessage = locationChatMessageFromAVMessage(message);
            Log.i(TAG, "地理消息");
        } else {
            chatMessage = textChatMessageFromAVMessage(message);
            Log.i(TAG, "未知类型消息");
        }
        Log.i(TAG, chatMessage.toString());
        return chatMessage;
    }

    /**
     * 将 AVIMTypedMessage 转换为 CommentNotifyMessage
     *
     * @param avMess 需要转换的 AVIMTypedMessage
     *
     * @return CommentNotifyMessage {@link CommentNotifyMessage}
     */
    private static CommentNotifyMessage dynCommentMessFromAVMessage(AVIMTextMessage avMess) {
        CommentNotifyMessage commentMess = new CommentNotifyMessage();
        mainMessageInfoFromAvimMessage(commentMess, avMess);
        commentMess.setReply_comment_id((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_comment_id));
        commentMess.setReply_comment((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_comment));
        commentMess.setReply_userid((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_userid));
        commentMess.setReply_username((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_username));

        commentMess.setDynid((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dynid));
        commentMess.setDyn_content((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dyn_content));
        commentMess.setDyn_icon((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dyn_icon));
        commentMess.setDyn_create_userid((String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dyn_create_userid));
        commentMess.setDyn_create_username((String) avMess.getAttrs()
                .get(StaticField.DynNoticeAttrName.dyn_create_username));
        return commentMess;
    }

    /**
     * 将 AVIMTypedMessage 转换为 SystemMessage
     *
     * @param avMess 需要转换的 AVIMTypedMessage
     *
     * @return SystemMessage {@link SystemMessage}
     */
    public static SystemMessage SysMessFromAVMess(AVIMTextMessage avMess) {
        SystemMessage systemMessage = new SystemMessage();
        mainMessageInfoFromAvimMessage(systemMessage, avMess);
        try {
            systemMessage.setJoinConvid((String) avMess.getAttrs().get(
                    StaticField.SystemMessAttrName.JOIN_CONV_ID));
        } catch (Exception ignored) {
        }
        systemMessage.setSenderID(((String) avMess.getAttrs().get(
                StaticField.ChatMessAttrName.userID)));
        systemMessage.setSenderIcon(((String) avMess.getAttrs().get(
                StaticField.ChatMessAttrName.userIcon)));
        systemMessage.setSenderName(((String) avMess.getAttrs().get(
                StaticField.ChatMessAttrName.userName)));
        systemMessage.setContent(avMess.getText());
        systemMessage.setRemark(((String) avMess.getAttrs().get(
                StaticField.SystemMessAttrName.REMARK)));
        systemMessage.setLink_url(((String) avMess.getAttrs().get(
                StaticField.SystemMessAttrName.LINK_URL)));
        systemMessage.setSys_msg_Type(SystemMessage.getSystemMsgType((int)
                avMess.getAttrs().get(StaticField.SystemMessAttrName.SYS_MSG_FLAG)));
        systemMessage.setCompleteStatue(SystemMessage.notComplete);
        systemMessage.setJoinGroupID((String) avMess.getAttrs().get(
                StaticField.ChatMessAttrName.groupID));
        systemMessage.setCreateTime(avMess.getTimestamp());
        return systemMessage;
    }

    /*转换位置消息*/
    @Deprecated
    private static ChatMessage locationChatMessageFromAVMessage(AVIMMessage message) {
        //        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        //        chatMessage.type = StaticField.ChatContantType.TEXT;
        //        AVGeoPoint avGeoPoint = ((AVIMLocationMessage) message).getLocation();
        //        chatMessage.text = ((AVIMLocationMessage) message).getText();
        //        chatMessage = setAttrsInfo(chatMessage, message);
        //        return chatMessage;
        return null;
    }

    /*转换文本消息*/
    private static ChatMessage textChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = new ChatMessage();
        mainMessageInfoFromAvimMessage(chatMessage, message);
        return chatMessage;
    }

    /*转换图片消息*/
    private static ImageChatMessage imageChatMessageFromAVMessage(AVIMMessage message) {
        ImageChatMessage imageChatMessage = new ImageChatMessage();
        mainMessageInfoFromAvimMessage(imageChatMessage, message);
        imageChatMessage.setImageUrl(((AVIMImageMessage) message).getFileUrl());
        imageChatMessage.setImageWeight(((AVIMImageMessage) message).getWidth());
        imageChatMessage.setImageHeight(((AVIMImageMessage) message).getHeight());
        return imageChatMessage;
    }

    /*转换音频消息*/
    private static VoiceChatMessage voiceChatMessageFromAVMessage(AVIMMessage message) {

        VoiceChatMessage voiceMess = new VoiceChatMessage();

        mainMessageInfoFromAvimMessage(voiceMess, message);
        voiceMess.setVoiceUrl(((AVIMAudioMessage) message).getFileUrl());
        voiceMess.setVoiceDuration(((AVIMAudioMessage) message).getDuration());
        return voiceMess;
    }

    /*转换视频消息*/
    @Deprecated
    private static ChatMessage vedioChatMessageFromAVMessage(AVIMMessage message) {
        VoiceChatMessage voiceMess = new VoiceChatMessage();

        mainMessageInfoFromAvimMessage(voiceMess, message);
        voiceMess.setVoiceUrl(((AVIMAudioMessage) message).getFileUrl());
        voiceMess.setVoiceDuration(((AVIMAudioMessage) message).getDuration());
        return voiceMess;
    }

    /*通用信息转换*/

    /**
     * @param message     需要转换的 AVIMMessage
     * @param chatMessage 转换结果
     *
     * @see ChatMessage
     */
    private static void mainMessageInfoFromAvimMessage(ChatMessage chatMessage, AVIMMessage message) {
        chatMessage.setMessID(message.getMessageId());
        chatMessage.setCreateTime(message.getTimestamp());
        chatMessage.setReceiptTime(message.getReceiptTimestamp());
        chatMessage.setStatus(ChatMessage.getMessageStatusType(message.getMessageStatus().getStatusCode()));
        chatMessage.setChatText(message.getContent());
        chatMessage.setConversationId(message.getConversationId());
        chatMessage.setSenderID(message.getFrom());
        setAttrsInfo(chatMessage, message);
    }

    /*Attrs*/

    /**
     * 从 attrs 获取信息,需要在 mainMessageInfoFromAvimMessage 后调用
     * 其他地方无需调用
     *
     * @param chatMessage 需要被设置的 ChatMessage
     * @param avimMessage 含有 attrs 的信息
     *
     * @return 设置完成的 ChatMessage
     */
    private static void setAttrsInfo(ChatMessage chatMessage, AVIMMessage avimMessage) {
        String TAG = "ChatMessFormatFromAVIM.setAttrsInfo";
        if (AVIMFileMessage.class.isInstance(avimMessage)) {
            Log.i(TAG, "AVIMFileMessage");
            AVIMFileMessage message = (AVIMFileMessage) avimMessage;
            chatMessage.setSenderIcon((String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userIcon));
            chatMessage.setSenderGroupID((String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.groupID));
            chatMessage.setSenderName((String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userName));
            chatMessage.setConversationType(ChatMessage.getMessageConversationType(
                    (int) message.getAttrs().get(StaticField.ChatMessAttrName.type)));
        } else if (AVIMTextMessage.class.isInstance(avimMessage)) {
            Log.i(TAG, "AVIMTextMessage");
            AVIMTextMessage message = (AVIMTextMessage) avimMessage;
            chatMessage.setSenderIcon((String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userIcon));
            chatMessage.setSenderGroupID((String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.groupID));
            chatMessage.setSenderName((String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userName));
            chatMessage.setConversationType(ChatMessage.getMessageConversationType(
                    (int) message.getAttrs().get(StaticField.ChatMessAttrName.type)));
        }

        /*判断消息来源类型*/
        if (chatMessage.getIsSelfSend()) {
            //自己发出的
            chatMessage.setFrom(ChatMessage.FROM_ME);
        } else if (chatMessage.getConversationType() != ChatMessage.CONVERSATION_TYPE_TEMP_GROUP) {
            //非碰撞
            chatMessage.setFrom(ChatMessage.FROM_OTHER_USER);
        } else {
            //碰撞圈子
            if (GroupList.getInstance().getGroup(chatMessage.getSenderGroupID()) != null) {
                chatMessage.setFrom(ChatMessage.FROM_GROUP_FRIEND);
            } else {
                chatMessage.setFrom(ChatMessage.FROM_OTHER_USER);
            }
        }
    }


}
