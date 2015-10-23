package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qixingchen on 15/8/26.
 * 新增 LeanCloud 聊天组
 */
public class NewAVIMConversation {

    private final static String TAG = NewAVIMConversation.class.getSimpleName();
    private static NewAVIMConversation mInstance;
    private ConversationCallBack conversationCallBack;

    private NewAVIMConversation() {
    }

    public static NewAVIMConversation getInstance() {
        if (mInstance == null) {
            synchronized (NewAVIMConversation.class) {
                if (mInstance == null) {
                    mInstance = new NewAVIMConversation();
                }
            }
        }
        return mInstance;
    }

    /**
     * 创建仅有用户自身的群，并通过回调返回群 conversationID
     *
     * @return mInstance
     */
    public NewAVIMConversation newAChatGroup() {
        List<String> clientIds = new ArrayList<>();
        clientIds.add(AppStaticValue.getUserID());


        Map<String, Object> attr = new HashMap<>();
        attr.put("type", StaticField.ConvType.GROUP);
        AVIMClient imClient = AppStaticValue.getImClient();
        imClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if (conversationCallBack != null) {
                    if (avimConversation != null) {
                        conversationCallBack.setConversationID(avimConversation.getConversationId());
                    } else {
                        conversationCallBack.setConversationID("0");
                    }
                } else {
                    Log.e(TAG, "conversationCallBack = null");
                }
            }
        });
        return mInstance;
    }

    /***
     * 新建单聊群
     */
    public void newAPrivateChat(String AnotherUser) {
        List<String> clientIds = new ArrayList<>();
        clientIds.add(AppStaticValue.getUserID());
        clientIds.add(AnotherUser);

        Map<String, Object> attr = new HashMap<>();
        attr.put("type", StaticField.ConvType.twoPerson);
        AVIMClient imClient = AppStaticValue.getImClient();
        imClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if (conversationCallBack != null) {
                    if (avimConversation != null) {
                        conversationCallBack.setConversationID(avimConversation.getConversationId());
                    } else {
                        conversationCallBack.setConversationID("0");
                    }
                } else {
                    Log.e(TAG, "conversationCallBack = null");
                    conversationCallBack.setConversationID("0");
                }
            }
        });
    }

    /**
     * 设置 conversationID 回调
     *
     * @param conversationCallBack 回调接口
     *
     * @return mInstance
     */
    public NewAVIMConversation setConversationCallBack(ConversationCallBack conversationCallBack) {
        this.conversationCallBack = conversationCallBack;
        return mInstance;

    }

    public interface ConversationCallBack {
        void setConversationID(String conversationID);
    }
}
