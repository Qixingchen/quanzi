package com.tizi.chatlibrary.model.message;

/**
 * Created by qixingchen on 16/2/24.
 * 消息转换
 */
public class ConvertMessage<T extends Object> {

    private static ConvertMessage mInstance;
    private Convert mConvert;

    public static ConvertMessage getInstance() {
        if (mInstance == null) {
            synchronized (ConvertMessage.class) {
                if (mInstance == null) {
                    mInstance = new ConvertMessage();
                }
            }
        }
        return mInstance;
    }

    public void setConvert(Convert mConvert) {
        this.mConvert = mConvert;
    }

    public ChatMessage convert(T t) {
        switch (mConvert.declearType(t)) {
            case ChatMessage.MESSAGE_TYPE_TEXT:
                return mConvert.convertToBaseMess(t);
            case ChatMessage.MESSAGE_TYPE_IMAGE:
                return mConvert.convertToImageMess(t);
            case ChatMessage.MESSAGE_TYPE_VOICE:
                return mConvert.convertToVoiceMess(t);
            default:
                return mConvert.convertToUndeclearType(t);
        }
    }

    public interface Convert<T> {
        /**
         * 将消息转换为图片消息
         */
        ImageChatMessage convertToImageMess(T t);

        /**
         * 将消息转换为语音消息
         */
        VoiceChatMessage convertToVoiceMess(T t);

        /**
         * 将消息转换为基础消息
         */
        ChatMessage convertToBaseMess(T t);

        /**
         * 将消息转换为自定义消息
         */
        ChatMessage convertToUndeclearType(T t);

        int declearType(T t);
    }


}
