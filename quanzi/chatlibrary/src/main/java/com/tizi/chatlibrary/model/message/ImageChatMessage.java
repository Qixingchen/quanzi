package com.tizi.chatlibrary.model.message;

import android.databinding.Bindable;

/**
 * Created by qixingchen on 16/2/23.
 * 带有图片的消息
 */
public class ImageChatMessage extends ChatMessage {

    private String imageUrl;/*图片在线地址*/
    private int imageHeight;/*图片高*/
    private int imageWeight;/*图片宽*/
    private String localPath;/*图片本地地址*/

    public ImageChatMessage() {
    }

    public ImageChatMessage(String localPath) {
        this.localPath = localPath;
    }

    public ImageChatMessage(int imageHeight, int imageWeight, String localPath) {
        this.imageHeight = imageHeight;
        this.imageWeight = imageWeight;
        this.localPath = localPath;
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Bindable
    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Bindable
    public int getImageWeight() {
        return imageWeight;
    }

    public void setImageWeight(int imageWeight) {
        this.imageWeight = imageWeight;
    }

    @Bindable
    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
