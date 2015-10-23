package com.tizi.quanzi.ui.dyns;

import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.chat.StartPrivateChat;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.tool.StaticField;

import java.util.Map;

/**
 * Created by qixingchen on 15/10/23.
 * 动态操作后,发布消息给相关人员
 */
public class DynActSendNotify {

    public static DynActSendNotify getNewInstance() {
        return new DynActSendNotify();
    }

    public void plusOne(String sourceUserID, final String dynID, final String dyn_content,
                        final Dyns.DynsEntity dyn) {
        StartPrivateChat.getNewInstance(new StartPrivateChat.GetConvID() {
            @Override
            public void onConvID(String convID) {
                Map<String, Object> attrs = setDynNotifiAttrs(convID, null, dyn);
                SendMessage.getInstance().sendTextMessage(convID, MyUserInfo.getInstance().getUserInfo().getUserName()
                        + "向您+1了", attrs);
            }

            @Override
            public void onError(String errorMessage) {

            }
        }).getPrivateChatConvID(sourceUserID);

    }

    private void replayDyn(final Comments.CommentsEntity comment, final String sourceUserID,
                           final Dyns.DynsEntity dyn) {
        StartPrivateChat.getNewInstance(new StartPrivateChat.GetConvID() {
            @Override
            public void onConvID(String convID) {
                Map<String, Object> attrs = setDynNotifiAttrs(convID, comment, dyn);
                SendMessage.getInstance().sendTextMessage(convID, MyUserInfo.getInstance().getUserInfo().getUserName()
                        + "向您+1了", attrs);
            }

            @Override
            public void onError(String errorMessage) {

            }
        }).getPrivateChatConvID(sourceUserID);
    }


    private Map<String, Object> setDynNotifiAttrs
            (String convID, String reply_comment_id, String reply_comment,
             String reply_userid, String reply_username, String dynid, String dyn_content, String dyn_icon,
             String dyn_create_userid, String dyn_create_username) {
        Map<String, Object> attrs = SendMessage.setMessAttr("", StaticField.ConvType.twoPerson);
        attrs = SendMessage.setSysMessAttr(attrs, convID, StaticField.SystemMessAttrName.systemFlag.dyn_comment, "");

        attrs.put("reply_comment_id", reply_comment_id);
        attrs.put("reply_comment", reply_comment);
        attrs.put("reply_userid", reply_userid);
        attrs.put("reply_username", reply_username);
        attrs.put("dynid", dynid);
        attrs.put("dyn_content", dyn_content);
        attrs.put("dyn_icon", dyn_icon);
        attrs.put("dyn_create_userid", dyn_create_userid);
        attrs.put("dyn_create_username", dyn_create_username);

        return attrs;
    }


    private Map<String, Object> setDynNotifiAttrs
            (String convID, String reply_comment_id, String reply_comment,
             String reply_userid, String reply_username, Dyns.DynsEntity dyn) {

        return setDynNotifiAttrs(convID, reply_comment_id, reply_comment, reply_userid, reply_username,
                dyn.dynid, dyn.content, dyn.icon, dyn.createUser, dyn.nickName);

    }

    private Map<String, Object> setDynNotifiAttrs(String convID, Comments.CommentsEntity comment, Dyns.DynsEntity dyn) {

        if (comment == null) {
            return setDynNotifiAttrs(convID, null, null, MyUserInfo.getInstance().getUserInfo().getId(),
                    MyUserInfo.getInstance().getUserInfo().getUserName(), dyn);
        }


        return setDynNotifiAttrs(convID, comment.id, comment.content, MyUserInfo.getInstance().getUserInfo().getId(),
                MyUserInfo.getInstance().getUserInfo().getUserName(), dyn);
    }

}
