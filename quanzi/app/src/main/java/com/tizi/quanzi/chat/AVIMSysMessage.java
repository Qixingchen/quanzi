package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Created by qixingchen on 15/8/21.
 */
@AVIMMessageType(type = 99)
public class AVIMSysMessage extends AVIMTypedMessage {

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    @AVIMMessageField(name = "msgType")
    private int msgType;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @AVIMMessageField(name = "remark")
    private String remark;


}

//
//class AVIMSysMessage: AVIMTypedMessage, AVIMTypedMessageSubclassing {
//
//        // 消息标志：区分通知、邀请加入、默认已经加入等消息
//        var msgType : AVIMSysMessageType!
//
//        // 描述
//        var remark : String?
//
//        // 加入会话
//        var joinConvId : String?
//
//        var image : String?
//
//        // 可能是一个链接
//        var url : String?
//
//static func classMediaType() -> AVIMMessageMediaType {
//        return ChatConstant.kAVIMMessageMediaTypeSystem
//        }
//        }