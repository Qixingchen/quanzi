package com.tizi.quanzi.chat;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMFileMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/8/14.
 * 将AVIM消息类型转换为ChatMessage 或systemMess
 */
public class ChatMessFormatFromAVIM {

    /**
     * 将 AVIMTypedMessage 转换为 ChatMessage
     *
     * @param message 需要转换的 AVIMTypedMessage
     *
     * @return ChatMessage {@link ChatMessage}
     *
     * @throws ClassFormatError 消息为系统消息
     */
    public static ChatMessage ChatMessageFromAVMessage(AVIMTypedMessage message) throws ClassFormatError {
        String TAG = "ChatMessFormatFromAVIM";
        ChatMessage chatMessage;
        if (message.getMessageType() == AVIMReservedMessageType.TextMessageType.getType()) {
            /*判断是否为系统消息*/
            String type = (String) ((AVIMTextMessage) message).getAttrs().get(
                    StaticField.ChatMessAttrName.IS_SYS_MESS);
            try {
                if (type.compareTo(StaticField.SystemMessAttrName.MessTypeCode.System_mess) == 0) {
                    throw new ClassFormatError("Is System Message");
                }
            } catch (Exception ignored) {//旧信息无此项目
            }
            chatMessage = textChatMessageFromAVMessage(message);
            Log.i(TAG, "文本消息:");
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
     * 将 AVIMTypedMessage 转换为 SystemMessage
     *
     * @param message 需要转换的 AVIMTypedMessage
     *
     * @return SystemMessage {@link SystemMessage}
     */
    public static SystemMessage SysMessFromAVMess(AVIMTypedMessage message) {
        SystemMessage systemMessage = new SystemMessage();
        AVIMTextMessage textMessage = (AVIMTextMessage) message;
        systemMessage.setId(textMessage.getMessageId());
        try {
            systemMessage.setConvid((String) textMessage.getAttrs().get(
                    StaticField.SystemMessAttrName.JOIN_CONV_ID));
        } catch (Exception ignored) {
        }
        systemMessage.user_id = ((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.userID));
        systemMessage.user_icon = ((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.userIcon));
        systemMessage.user_name = ((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.userName));
        systemMessage.msg_type = ((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.IS_SYS_MESS));
        systemMessage.setContent(textMessage.getText());
        systemMessage.remark = ((String) textMessage.getAttrs().get(
                StaticField.SystemMessAttrName.REMARK));
        systemMessage.link_url = ((String) textMessage.getAttrs().get(
                StaticField.SystemMessAttrName.LINK_URL));
        systemMessage.sys_msg_flag = ((int) textMessage.getAttrs().get(
                StaticField.SystemMessAttrName.SYS_MSG_FLAG));
        systemMessage.setStatus(0);
        systemMessage.setIsread(false);
        systemMessage.setGroup_id((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.groupID));
        systemMessage.create_time = (textMessage.getTimestamp());
        if (systemMessage.sys_msg_flag == StaticField.SystemMessAttrName.systemFlag.dyn_comment) {
            systemMessage = dynNoticeFromAVmess(textMessage, systemMessage);
        }
        return systemMessage;
    }

    /**
     * 将 AVIMTypedMessage 转换为 SystemMessage
     *
     * @param avMess  需要转换的 AVIMTypedMessage
     * @param sysMess avmess 已经通过 SysMessFromAVMess 转换的 SystemMessage
     *
     * @return {@link SystemMessage}
     */
    private static SystemMessage dynNoticeFromAVmess(AVIMTextMessage avMess, SystemMessage sysMess) {

        sysMess.reply_comment_id = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_comment_id);
        sysMess.reply_comment = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_comment);
        sysMess.reply_userid = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_userid);
        sysMess.reply_username = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.reply_username);

        sysMess.dynid = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dynid);
        sysMess.dyn_content = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dyn_content);
        sysMess.dyn_icon = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dyn_icon);
        sysMess.dyn_create_userid = (String) avMess.getAttrs().get(StaticField.DynNoticeAttrName.dyn_create_userid);
        sysMess.dyn_create_username = (String) avMess.getAttrs()
                .get(StaticField.DynNoticeAttrName.dyn_create_username);

        return sysMess;
    }

    /*转换位置消息*/
    @Deprecated
    private static ChatMessage locationChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.TEXT;
        AVGeoPoint avGeoPoint = ((AVIMLocationMessage) message).getLocation();
        chatMessage.text = ((AVIMLocationMessage) message).getText();
        chatMessage = setAttrsInfo(chatMessage, message);
        return chatMessage;
    }

    /*转换文本消息*/
    private static ChatMessage textChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.TEXT;
        chatMessage.text = ((AVIMTextMessage) message).getText();
        chatMessage = setAttrsInfo(chatMessage, message);
        return chatMessage;
    }

    /*转换图片消息*/
    private static ChatMessage imageChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.IMAGE;
        chatMessage.url = ((AVIMImageMessage) message).getFileUrl();
        chatMessage = setAttrsInfo(chatMessage, message);
        chatMessage.imageWeight = ((AVIMImageMessage) message).getWidth();
        chatMessage.imageHeight = ((AVIMImageMessage) message).getHeight();
        return chatMessage;
    }

    /*转换音频消息*/
    private static ChatMessage voiceChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage = setAttrsInfo(chatMessage, message);
        chatMessage.type = StaticField.ChatContantType.VOICE;
        chatMessage.url = ((AVIMAudioMessage) message).getFileUrl();
        chatMessage.voice_duration = ((AVIMAudioMessage) message).getDuration();
        chatMessage.chatImage = (String)
                ((AVIMAudioMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userIcon);
        chatMessage.userName = (String)
                ((AVIMAudioMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userName);
        chatMessage.groupID = (String)
                ((AVIMAudioMessage) message).getAttrs().get(StaticField.ChatMessAttrName.groupID);
        return chatMessage;
    }

    /*转换视频消息*/
    private static ChatMessage vedioChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.VEDIO;
        chatMessage.url = ((AVIMVideoMessage) message).getFileUrl();
        chatMessage.voice_duration = ((AVIMVideoMessage) message).getDuration();
        chatMessage = setAttrsInfo(chatMessage, message);
        return chatMessage;
    }

    /*通用信息转换*/

    /**
     * @param message 需要转换的 AVIMMessage
     *
     * @return 部分信息已经填充的 ChatMessage
     *
     * @see ChatMessage
     */
    private static ChatMessage mainMessageInfoFromAvimMessage(AVIMMessage message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.messID = message.getMessageId();
        chatMessage.create_time = message.getTimestamp();
        chatMessage.receiptTimestamp = message.getReceiptTimestamp();
        chatMessage.status = message.getMessageStatus().getStatusCode();
        chatMessage.text = message.getContent();
        chatMessage.ConversationId = message.getConversationId();
        chatMessage.uid = AppStaticValue.getUserID();
        chatMessage.sender = message.getFrom();
        // TODO: 15/8/13  getMessageIOType不可用 为什么？
        chatMessage.isSelfSend = (message.getFrom().compareTo(AppStaticValue.getUserID()) == 0);
        chatMessage.isread = chatMessage.isSelfSend;

        return chatMessage;
    }

    /*Attrs*/

    /**
     * 从 attrs 获取信息,需要在 mainMessageInfoFromAvimMessage 后调用
     *
     * @param chatMessage 需要被设置的 ChatMessage
     * @param avimMessage 含有 attrs 的信息
     *
     * @return 设置完成的 ChatMessage
     */
    private static ChatMessage setAttrsInfo(ChatMessage chatMessage, AVIMMessage avimMessage) {
        String TAG = "ChatMessFormatFromAVIM.setAttrsInfo";
        if (AVIMFileMessage.class.isInstance(avimMessage)) {
            Log.i(TAG, "AVIMFileMessage");
            AVIMFileMessage message = (AVIMFileMessage) avimMessage;
            chatMessage.chatImage = (String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userIcon);
            chatMessage.userName = (String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userName);
            chatMessage.groupID = (String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.groupID);
            chatMessage.ChatBothUserType = (int)
                    message.getAttrs().get(StaticField.ChatMessAttrName.type);
        } else if (AVIMTextMessage.class.isInstance(avimMessage)) {
            Log.i(TAG, "AVIMTextMessage");
            AVIMTextMessage message = (AVIMTextMessage) avimMessage;
            chatMessage.chatImage = (String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userIcon);
            chatMessage.userName = (String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.userName);
            chatMessage.groupID = (String)
                    message.getAttrs().get(StaticField.ChatMessAttrName.groupID);
            chatMessage.ChatBothUserType = (int)
                    message.getAttrs().get(StaticField.ChatMessAttrName.type);
        }

        /*判断消息来源类型*/
        if (chatMessage.isSelfSend) {
            //自己发出的
            chatMessage.From = StaticField.ChatFrom.ME;
        } else if (chatMessage.ChatBothUserType != StaticField.ConvType.BoomGroup) {
            //非碰撞
            chatMessage.From = StaticField.ChatFrom.OTHER;
        } else {
            //碰撞圈子
            BoomGroupClass boomGroup = BoomGroupList.getInstance().getGroup(chatMessage.groupID);
            boolean isMyGroupContainSend = false;
            if (boomGroup != null) {
                if (boomGroup.isGroup1MyGroup) {
                    //圈子1是自己的圈子
                    for (BoomGroup.GroupmatchEntity.GrpmemEntity member : boomGroup.groupMenber1) {
                        if (member.id.compareTo(chatMessage.sender) == 0) {
                            isMyGroupContainSend = true;
                            break;
                        }
                    }
                } else {
                    //圈子2是自己的圈子
                    for (BoomGroup.GroupmatchEntity.GrpmemEntity member : boomGroup.groupMenber2) {
                        if (member.id.compareTo(chatMessage.sender) == 0) {
                            isMyGroupContainSend = true;
                            break;
                        }
                    }
                }
            }
            //如果是圈内好友，来源是圈子，否则是其他
            if (isMyGroupContainSend) {
                chatMessage.From = StaticField.ChatFrom.GROUP;
            } else {
                chatMessage.From = StaticField.ChatFrom.OTHER;
            }
        }

        return chatMessage;
    }


}
