package com.tizi.quanzi.ui.dyns;

import android.support.annotation.Nullable;

import com.tizi.quanzi.app.AppStaticValue;
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

    /**
     * +1 的通知
     *
     * @param dyn    被+1的动态
     * @param isUser 是否是朋友圈动态
     */
    public void plusOne(final Dyns.DynsEntity dyn, final boolean isUser) {
        if (dyn.createUser.compareTo(AppStaticValue.getUserID()) == 0) {
            return;
        }
        StartPrivateChat.getNewInstance(new StartPrivateChat.GetConvID() {
            @Override
            public void onConvID(String convID) {
                Map<String, Object> attrs = setDynNotifiAttrs(null, dyn, isUser);
                SendMessage.getNewInstance().sendTextMessage(convID, MyUserInfo.getInstance().getUserInfo().getUserName()
                        + "对你点赞了", attrs);
            }

            @Override
            public void onError(String errorMessage) {

            }
        }).getPrivateChatConvID(dyn.createUser);

    }

    /**
     * 回复动态
     *
     * @param comment 回复的内容
     * @param dyn     动态
     * @param isUser  是否是朋友圈动态
     */
    public void replayDyn(final String comment, final Dyns.DynsEntity dyn, final boolean isUser) {
        replayComment(comment, null, dyn, isUser);
    }

    /**
     * 回复评论
     *
     * @param comment        回复的内容
     * @param commentsEntity 被回复的评论
     * @param dyn            动态
     * @param isUser         是否是朋友圈动态
     */
    public void replayComment(final String comment, final Comments.CommentsEntity commentsEntity,
                              final Dyns.DynsEntity dyn, final boolean isUser) {
        if (dyn.createUser.compareTo(AppStaticValue.getUserID()) == 0) {
            return;
        }
        StartPrivateChat.getNewInstance(new StartPrivateChat.GetConvID() {
            @Override
            public void onConvID(String convID) {
                Map<String, Object> attrs = setDynNotifiAttrs(commentsEntity, dyn, isUser);
                SendMessage.getNewInstance().sendTextMessage(convID, comment, attrs);
            }

            @Override
            public void onError(String errorMessage) {

            }
        }).getPrivateChatConvID(dyn.createUser);
    }


    /**
     * 提及用户
     *
     * @param comment        评论内容
     * @param atUserid       被提及的用户ID
     * @param commentsEntity (如有)被回复的评论
     * @param dyn            动态
     * @param isUser         是否是朋友圈动态
     */
    public void atUser(final String comment, String atUserid, @Nullable final Comments.CommentsEntity commentsEntity,
                       final Dyns.DynsEntity dyn, final boolean isUser) {
        if (atUserid.compareTo(AppStaticValue.getUserID()) == 0) {
            return;
        }
        /**
         * at的是动态发布者的话,不通知,因为已经通知过了
         * */
        if (atUserid.compareTo(dyn.createUser) == 0) {
            return;
        }
        StartPrivateChat.getNewInstance(new StartPrivateChat.GetConvID() {
            @Override
            public void onConvID(String convID) {
                Map<String, Object> attrs = setDynNotifiAttrs(commentsEntity, dyn, isUser);
                SendMessage.getNewInstance().sendTextMessage(convID, comment, attrs);
            }

            @Override
            public void onError(String errorMessage) {

            }
        }).getPrivateChatConvID(atUserid);
    }

    /**
     * 设置attrs
     *
     * @param reply_comment_id    如果回复的是评论,它的ID,不是则为空
     * @param reply_comment       如果回复的是评论,它的内容,不是则为空
     * @param reply_userid        如果回复的是评论,他的发布者的ID,不是则为空
     * @param reply_username      如果回复的是评论,他的发布者的名字,不是则为空
     * @param dynid               动态的ID
     * @param dyn_content         动态的内容
     * @param dyn_icon            动态的图片(如果动态不带图,则是发布者的头像,否则是第一张图)
     * @param dyn_create_userid   动态发布者的ID
     * @param dyn_create_username 动态发布者的名字
     * @param isUser              是否是朋友圈动态
     *
     * @return attrs
     */
    private Map<String, Object> setDynNotifiAttrs
    (String reply_comment_id, String reply_comment,
     String reply_userid, String reply_username, String dynid, String dyn_content, String dyn_icon,
     String dyn_create_userid, String dyn_create_username, boolean isUser) {
        Map<String, Object> attrs = SendMessage.setMessAttr("", StaticField.ConvType.twoPerson);
        attrs = SendMessage.setSysMessAttr(attrs, null, StaticField.SystemMessAttrName.systemFlag.dyn_comment, "");

        attrs.put("reply_comment_id", reply_comment_id);
        attrs.put("reply_comment", reply_comment);
        attrs.put("reply_userid", reply_userid);
        attrs.put("reply_username", reply_username);
        attrs.put("dynid", dynid);
        attrs.put("dyn_content", dyn_content);
        attrs.put("dyn_icon", dyn_icon);
        if (isUser) {
            attrs.put("dyn_create_userid", dyn_create_userid);
        } else {
            attrs.put("dyn_create_userid", "");
        }
        attrs.put("dyn_create_username", dyn_create_username);

        return attrs;
    }

    /**
     * 设置attrs
     *
     * @param reply_comment_id 如果回复的是评论,它的ID,不是则为空
     * @param reply_comment    如果回复的是评论,它的内容,不是则为空
     * @param reply_userid     如果回复的是评论,他的发布者的ID,不是则为空
     * @param reply_username   如果回复的是评论,他的发布者的名字,不是则为空
     * @param dyn              动态
     * @param isUser           是否是朋友圈动态
     *
     * @return attrs
     */
    private Map<String, Object> setDynNotifiAttrs
    (String reply_comment_id, String reply_comment,
     String reply_userid, String reply_username, Dyns.DynsEntity dyn, boolean isUser) {

        String icon = dyn.pics.size() == 0 ? dyn.icon : dyn.pics.get(0).url;

        return setDynNotifiAttrs(reply_comment_id, reply_comment, reply_userid, reply_username,
                dyn.dynid, dyn.content, icon, dyn.createUser, dyn.nickName, isUser);

    }

    /**
     * 设置attrs
     *
     * @param comment 被回复的评论,可以为null
     * @param dyn     动态
     * @param isUser  是否是朋友圈动态
     *
     * @return attrs
     */
    private Map<String, Object> setDynNotifiAttrs(@Nullable Comments.CommentsEntity comment, Dyns.DynsEntity dyn, boolean isUser) {

        if (comment == null) {
            return setDynNotifiAttrs(null, null, MyUserInfo.getInstance().getUserInfo().getId(),
                    MyUserInfo.getInstance().getUserInfo().getUserName(), dyn, isUser);
        }


        return setDynNotifiAttrs(comment.id, comment.content, MyUserInfo.getInstance().getUserInfo().getId(),
                MyUserInfo.getInstance().getUserInfo().getUserName(), dyn, isUser);
    }

}
