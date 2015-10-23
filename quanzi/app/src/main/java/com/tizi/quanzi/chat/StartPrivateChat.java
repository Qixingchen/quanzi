package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/10/23.
 */
public class StartPrivateChat {

    private GetConvID getConvID;

    public static StartPrivateChat getNewInstance(GetConvID getConvID) {
        StartPrivateChat startPrivateChat = new StartPrivateChat();
        startPrivateChat.getConvID = getConvID;
        return startPrivateChat;
    }

    public void getPrivateChatConvID(final String userID) {

        List<String> members = new ArrayList<>();
        members.add(AppStaticValue.getUserID());
        members.add(userID);

        AVIMConversationQuery query = AppStaticValue.getImClient().getQuery();
        query.whereEqualTo("attr.type", StaticField.ConvType.twoPerson);
        query.withMembers(members);
        query.whereSizeEqual("m", 2);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (e != null) {
                    if (getConvID != null) {
                        getConvID.onError(e.getMessage());
                    }
                }

                switch (list.size()) {
                    case 0:
                        NewAVIMConversation.getInstance().setConversationCallBack(
                                new NewAVIMConversation.ConversationCallBack() {
                                    @Override
                                    public void setConversationID(String conversationID) {
                                        if (getConvID != null) {
                                            if (conversationID.compareTo("0") == 0) {
                                                getConvID.onError("LC异常");
                                            } else {
                                                getConvID.onConvID(conversationID);
                                            }
                                        }
                                    }
                                }).newAPrivateChat(userID);
                        break;
                    case 1:
                        if (getConvID != null) {
                            getConvID.onConvID(list.get(0).getConversationId());
                        }
                        break;
                    default:
                        if (getConvID != null) {
                            getConvID.onConvID(list.get(0).getConversationId());
                        }
                        StringBuilder convIDs = new StringBuilder();
                        for (AVIMConversation conversation : list) {
                            convIDs.append(conversation.getConversationId()).append("\n");
                        }
                        Log.e(StartPrivateChat.class.getSimpleName(), "发现多个私聊ConvID：" + convIDs);
                }
            }
        });


    }

    public interface GetConvID {
        void onConvID(String convID);

        void onError(String errorMessage);
    }

}
