package com.tizi.chatlibrary.model.message;

import android.databinding.Bindable;

/**
 * Created by qixingchen on 16/2/23.
 */
public class VoiceChatMessage extends ChatMessage {
    private String voiceUrl;/*音频地址*/
    private String localPath;/*本地地址*/
    private double voiceDuration;/*音频时长*/

    public VoiceChatMessage() {
    }

    public VoiceChatMessage(String localPath) {
        this.localPath = localPath;
    }

    @Bindable
    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    @Bindable
    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Bindable
    public double getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(double voiceDuration) {
        this.voiceDuration = voiceDuration;
    }
}
