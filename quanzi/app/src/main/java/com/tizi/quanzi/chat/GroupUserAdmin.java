package com.tizi.quanzi.chat;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qixingchen on 15/9/3.
 * 管理群用户
 */
public class GroupUserAdmin {
    private Context mContext;
    private OnResult onResult;

    public static GroupUserAdmin getInstance(Context context) {
        return new GroupUserAdmin(context);
    }

    private GroupUserAdmin(Context context) {
        this.mContext = context;
    }

    public GroupUserAdmin setOnResult(OnResult onResult) {
        this.onResult = onResult;
        return this;
    }

    public void addMember(String convID, String groupID, String userID) {
        // TODO: 15/9/3 setUserMess
        Map<String, Object> attr = SendMessage.setMessAttr();
        attr = SendMessage.setGroupManageSysMessAttr(attr, convID,
                StaticField.SystemMessAttrName.systemFlag.invitation);

        List<String> clientIds = new ArrayList<>();
        clientIds.add(App.getUserID());
        clientIds.add(userID);

        Map<String, Object> chatAttr = new HashMap<String, Object>();
        attr.put("type", StaticField.ChatBothUserType.twoPerson);
        final Map<String, Object> finalAttr = attr;
        App.getImClient().createConversation(clientIds, chatAttr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVException e) {
                if (e != null) {
                    if (onResult != null) {
                        onResult.error(e.getMessage());
                    }
                    return;
                }
                SendMessage.getInstance().setSendOK(
                        new SendMessage.SendOK() {
                            @Override
                            public void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID) {
                                if (onResult != null) {
                                    onResult.OK();
                                }
                            }

                            @Override
                            public void sendError(String errorMessage, String CONVERSATION_ID) {
                                if (onResult != null) {
                                    onResult.error(errorMessage);
                                }
                            }
                        }
                ).sendTextMessage(avimConversation.getConversationId(), "邀请你来玩", finalAttr);
            }
        });


    }

    public void deleteMember(String convID, String groupID, String userID) {

    }

    public void deleteGroup(String convID, String groupID) {

    }

    public interface OnResult {
        void OK();

        void error(String errorMessage);
    }

}
