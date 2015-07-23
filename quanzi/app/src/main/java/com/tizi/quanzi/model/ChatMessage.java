package com.tizi.quanzi.model;

import com.tizi.quanzi.tool.StaticField;

import java.util.Random;

/**
 * Created by qixingchen on 15/7/20.
 */
public class ChatMessage {
    public String chatUserName = "", chatTime = "", chatMessage = "", chatImage = "";
    public int chatFrom;//消息来源，分为 自己，群内，对方

    private static String[] photoUri = {"http://cdn.marketplaceimages.windowsphone.com/v8/images/f5f28d0a-bef3-4bed-99ef-d2f21d624e3b?imageType=ws_icon_large",
            "http://ac-iz9otzx1.clouddn.com/l482ke5VNLyVQplRAmfoknrRc7azPWch3B8AstWw.jpg",
            "http://ac-iz9otzx1.clouddn.com/j0OtVC4rkrAIL7h2mgwCylofS3bEqsrOEGdNEqVm.jpg",
            "http://ac-iz9otzx1.clouddn.com/lo73gXLe1hsXP93fGs0m4TMibivViSLY6qN4Pt3A.jpg"};

    public static ChatMessage[] getChatMess() {
        ChatMessage chatMessages[] = new ChatMessage[20000];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 20000; i++) {
            chatMessages[i] = new ChatMessage();
            chatMessages[i].chatMessage = "消息测试，这是第几条消息" + i
                    + photoUri[random.nextInt(photoUri.length)];
            chatMessages[i].chatFrom = random.nextInt(3);
            switch (chatMessages[i].chatFrom) {

                case StaticField.ChatType.OTHER:
                    chatMessages[i].chatImage = photoUri[random.nextInt(photoUri.length)];
                    break;
                case StaticField.ChatType.ME:
                    chatMessages[i].chatImage = "http://www.gravatar.com/avatar/6727fb208dd4a54b0eac56f8f6142cda?s=500";
                    break;
                default:
                    chatMessages[i].chatImage = photoUri[random.nextInt(photoUri.length)];
            }

            chatMessages[i].chatUserName = "发行者" + i;
            chatMessages[i].chatTime = "08:" + i;
        }

        return chatMessages;
    }
}
