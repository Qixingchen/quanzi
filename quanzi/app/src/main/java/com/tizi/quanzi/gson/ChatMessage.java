package com.tizi.quanzi.gson;

/**
 * Created by qixingchen on 15/7/20.
 */
public class ChatMessage {
    public String chatUserName, chatTime, chatMessage, chatImage;

    public static ChatMessage[] getChatMess() {
        ChatMessage chatMessages[] = new ChatMessage[20];
        for (int i = 0; i < 20; i++) {
            chatMessages[i].chatMessage = "消息测试，这是第几条消息" + i;
            chatMessages[i].chatImage = "http://cdn.marketplaceimages.windowsphone.com/v8/images/f5f28d0a-bef3-4bed-99ef-d2f21d624e3b?imageType=ws_icon_large";
            chatMessages[i].chatUserName = "发行者" + i;
            chatMessages[i].chatTime = "08:" + i;
        }

        return chatMessages;
    }
}
