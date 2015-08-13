package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;

import java.util.List;

/**
 * Created by qixingchen on 15/8/12.
 * 成员变化
 */
public class MyAVIMConversationEventHandler extends AVIMConversationEventHandler {
    // TODO: 15/8/12
    @Override
    public void onMemberLeft(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
        
    }

    @Override
    public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {

    }

    @Override
    public void onKicked(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

    }

    @Override
    public void onInvited(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

    }
}
