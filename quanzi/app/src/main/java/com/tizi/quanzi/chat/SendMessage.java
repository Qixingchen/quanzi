package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.model.message.ImageChatMessage;
import com.tizi.chatlibrary.model.message.VoiceChatMessage;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by qixingchen on 15/9/2.
 * 发送信息的调用接口
 */
public class SendMessage {
    private SendMessage mInstance;
    private ChatViewSendOK chatViewSendOK;


    private SendMessage() {
        mInstance = this;
    }

    public static SendMessage getNewInstance() {
        return new SendMessage();
    }

    /**
     * 设置附加参数
     *
     * @param ChatBothUserType {@link StaticField.ConvType}
     *
     * @return 聊天时需要附加的参数
     */
    private static Map<String, Object> setMessAttr(String userID, String userName, String userIcon, String groupID,
                                                   int ChatBothUserType, boolean isNotifi) {
        Map<String, Object> attr = new TreeMap<>();
        attr.put(StaticField.ChatMessAttrName.userName, userName);
        attr.put(StaticField.ChatMessAttrName.userIcon, userIcon);
        attr.put(StaticField.ChatMessAttrName.userID, userID);
        attr.put(StaticField.ChatMessAttrName.groupID, groupID);
        attr.put(StaticField.ChatMessAttrName.type, ChatBothUserType);
        if (isNotifi) {
            attr.put(StaticField.SystemMessAttrName.SYS_MSG_FLAG, StaticField.SystemMessAttrName.systemFlag.notice);
        }
        attr.put(StaticField.ChatMessAttrName.IS_SYS_MESS,
                StaticField.SystemMessAttrName.MessTypeCode.normal_mess);
        return attr;
    }

    public static Map<String, Object> setMessAttr(String groupID, int ChatBothUserType) {
        return setMessAttr(groupID, ChatBothUserType, false);
    }

    public static Map<String, Object> setMessAttr(String groupID, int ChatBothUserType, boolean isNotifi) {
        Login.UserEntity my = MyUserInfo.getInstance().getUserInfo();
        if (my == null) {
            return null;
        }
        return setMessAttr(my.getId(), my.getUserName(), my.getIcon(), groupID, ChatBothUserType, isNotifi);
    }

    /**
     * 设置系统消息的附加参数
     *
     * @param attr            原有参数列
     * @param CONVERSATION_ID 需要加入的convID
     * @param systemFlag      {@link com.tizi.quanzi.tool.StaticField.SystemMessAttrName.systemFlag}
     * @param remark          附注内容
     *
     * @return 添加了系统参数的列
     */
    public static Map<String, Object> setSysMessAttr(Map<String, Object> attr,
                                                     String CONVERSATION_ID, int systemFlag, String remark) {
        attr.put(StaticField.ChatMessAttrName.IS_SYS_MESS,
                StaticField.SystemMessAttrName.MessTypeCode.System_mess);
        attr.put(StaticField.SystemMessAttrName.REMARK, remark);
        attr.put(StaticField.SystemMessAttrName.JOIN_CONV_ID, CONVERSATION_ID);
        attr.put(StaticField.SystemMessAttrName.LINK_URL, "");
        attr.put(StaticField.SystemMessAttrName.SYS_MSG_FLAG,
                systemFlag);
        return attr;
    }

    public SendMessage setChatViewSendOK(ChatViewSendOK chatViewSendOK) {
        this.chatViewSendOK = chatViewSendOK;
        return this;
    }

    /**
     * 发送图片消息
     *
     * @param Filepath 图片地址
     */
    public void sendImageMesage(final String convID, final String Filepath, Map<String, Object> attr, String groupID) {
        AVIMImageMessage message;
        try {
            message = new AVIMImageMessage(Filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        message.setAttrs(attr);
        final String tempID = UUID.randomUUID().toString();
        Login.UserEntity user = MyUserInfo.getInstance().getUserInfo();
        if (user == null) {
            return;
        }
        final ImageChatMessage imageChatMessage = new ImageChatMessage(Filepath);
        ChatMessage.setChatMessage(imageChatMessage, AppStaticValue.getUserID(), ChatMessage.MESSAGE_TYPE_IMAGE,
                (int) attr.get(StaticField.ChatMessAttrName.type), "", convID, tempID, Tool.getBeijinTime(),
                user.getUserName(), user.getIcon(), groupID);

        onChatViewMessagePreSend(imageChatMessage, convID);

        final AVIMImageMessage finalMessage = message;
        final AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
        conversation.sendMessage(message,
                new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (null != e) {
                            if (BuildConfig.DEBUG) {
                                onMessageSendError(e.getMessage(), convID, tempID, imageChatMessage);
                            } else {
                                onMessageSendError("网络错误", convID, tempID, imageChatMessage);
                                e.printStackTrace();
                            }
                        } else {
                            sendOK(finalMessage, conversation.getConversationId(), tempID, Filepath);
                        }
                    }
                }

        );
    }

    /**
     * 发送音频消息
     *
     * @param Filepath 音频地址
     */
    public void sendAudioMessage(final String convID, final String Filepath, Map<String, Object> attr, String groupID) {
        try {
            AVIMAudioMessage message = new AVIMAudioMessage(Filepath);
            message.setAttrs(attr);
            final String tempID = UUID.randomUUID().toString();
            Login.UserEntity user = MyUserInfo.getInstance().getUserInfo();
            if (user == null) {
                return;
            }

            final VoiceChatMessage chatMessage = new VoiceChatMessage(Filepath);
            ChatMessage.setChatMessage(chatMessage, AppStaticValue.getUserID(), ChatMessage.MESSAGE_TYPE_IMAGE,
                    (int) attr.get(StaticField.ChatMessAttrName.type), "", convID, tempID, Tool.getBeijinTime(),
                    user.getUserName(), user.getIcon(), groupID);

            onChatViewMessagePreSend(chatMessage, convID);

            final AVIMAudioMessage finalMessage = message;
            final AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
            conversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e != null) {
                        if (BuildConfig.DEBUG) {
                            onMessageSendError(e.getMessage(), convID, tempID, chatMessage);
                        } else {
                            onMessageSendError("网络错误", convID, tempID, chatMessage);
                            e.printStackTrace();
                        }
                    } else {
                        sendOK(finalMessage, conversation.getConversationId(), tempID, Filepath);
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 发送文本消息
     *
     * @param text 发送的文本
     */
    public void sendTextMessage(final String convID, String text, Map<String, Object> attr, String groupID) {
        final AVIMTextMessage message = new AVIMTextMessage();
        message.setText(text);
        message.setAttrs(attr);
        final String tempID = UUID.randomUUID().toString();
        Login.UserEntity user = MyUserInfo.getInstance().getUserInfo();
        if (user == null) {
            return;
        }

        final ChatMessage chatMessage = new ChatMessage();
        ChatMessage.setChatMessage(chatMessage, AppStaticValue.getUserID(), ChatMessage.MESSAGE_TYPE_IMAGE,
                (int) attr.get(StaticField.ChatMessAttrName.type), "", convID, tempID, Tool.getBeijinTime(),
                user.getUserName(), user.getIcon(), groupID);


        onChatViewMessagePreSend(chatMessage, convID);
        final AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
        conversation.sendMessage(message,
                new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (null != e) {
                            if (BuildConfig.DEBUG) {
                                onMessageSendError(e.getMessage(), convID, tempID, chatMessage);
                            } else {
                                onMessageSendError("网络错误", convID, tempID, chatMessage);
                                e.printStackTrace();
                            }
                        } else {
                            sendOK(message, conversation.getConversationId(), tempID, null);
                        }
                    }
                }

        );
    }

    /*RX*/
    public Observable<RXSendOK> sendRXTextMessage(
            final String convID, final String text, final Map<String, Object> attr) {

        Observable<RXSendOK> okObservable = Observable.create(new Observable.OnSubscribe<RXSendOK>() {
            @Override
            public void call(final Subscriber<? super RXSendOK> subscriber) {
                final AVIMTextMessage message = new AVIMTextMessage();
                message.setText(text);
                message.setAttrs(attr);
                final AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
                conversation.sendMessage(message,
                        new AVIMConversationCallback() {
                            @Override
                            public void done(AVIMException e) {
                                if (null != e) {
                                    if (BuildConfig.DEBUG) {
                                        onMessageSendError(e.getMessage(), convID, "", null);
                                    } else {
                                        onMessageSendError("网络错误", convID, "", null);
                                        e.printStackTrace();
                                    }
                                } else {
                                    subscriber.onNext(new RXSendOK(message, conversation.getConversationId()));
                                }
                            }
                        }

                );
            }
        });
        return okObservable;

    }

    private void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID, String tempID, String localFile) {
        if (chatViewSendOK != null) {
            onChatViewMessageSendOK(Message, CONVERSATION_ID, tempID);
        }
    }

    private void onChatViewMessagePreSend(ChatMessage chatMessage, String CONVERSATION_ID) {
        if (chatViewSendOK == null) {
            return;
        }

        Log.d("预发送", chatMessage.toString());
        com.tizi.chatlibrary.action.SendMessage.onMessagePreSend(CONVERSATION_ID, chatMessage.getMessID(), chatMessage);

        chatViewSendOK.preSend(chatMessage, CONVERSATION_ID);

    }

    private void onChatViewMessageSendOK(AVIMTypedMessage Message, String CONVERSATION_ID,
                                         String tempID) {
        ChatMessage chatMessage =
                ChatMessFormatFromAVIM.ChatMessageFromAVMessage(Message);
        com.tizi.chatlibrary.action.SendMessage.onMessageSendOK(tempID, chatMessage, CONVERSATION_ID);
        if (chatViewSendOK != null) {
            chatViewSendOK.sendOK(chatMessage, CONVERSATION_ID, tempID);
        }
    }

    private void onMessageSendError(String errorMessage, String CONVERSATION_ID,
                                    String tempID, ChatMessage chatMessage) {

        chatMessage.setStatus(ChatMessage.STATUS_FAILED);

        if (chatViewSendOK != null) {
            chatViewSendOK.sendError(errorMessage, CONVERSATION_ID, tempID, chatMessage);
        }
    }

    /*用于在聊天进行时的回调*/
    public interface ChatViewSendOK {
        /**
         * @param Message         AVIM 消息
         * @param CONVERSATION_ID 消息对应的CONVID
         * @param tempID          此消息对应的临时ID
         */
        void sendOK(ChatMessage Message, String CONVERSATION_ID, String tempID);

        /*正在发送 不保证是否成功*/
        void preSend(ChatMessage Message, String CONVERSATION_ID);

        void sendError(String errorMessage, String CONVERSATION_ID, String tempID, ChatMessage Message);
    }

    public class RXSendOK {
        public AVIMTypedMessage Message;
        public String CONVERSATION_ID;

        public RXSendOK(AVIMTypedMessage message, String CONVERSATION_ID) {
            Message = message;
            this.CONVERSATION_ID = CONVERSATION_ID;
        }
    }
}
