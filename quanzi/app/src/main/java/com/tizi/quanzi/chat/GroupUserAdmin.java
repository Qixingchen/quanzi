package com.tizi.quanzi.chat;

import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.GroupInviteAns;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.RetrofitAPI;
import com.tizi.quanzi.network.RetrofitNetwork;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.network.UserManageInGroup;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by qixingchen on 15/9/3.
 * 管理群用户
 */
public class GroupUserAdmin {
    private final static String TAG = GroupUserAdmin.class.getSimpleName();
    private Context mContext;
    private OnResult onResult;

    private GroupUserAdmin(Context context) {
        this.mContext = context;
    }

    public static GroupUserAdmin getInstance(Context context) {
        return new GroupUserAdmin(context);
    }

    /**
     * 设置回调
     *
     * @param onResult 回调函数
     */
    public GroupUserAdmin setOnResult(OnResult onResult) {
        this.onResult = onResult;
        return this;
    }

    /**
     * 向组内邀请成员
     *
     * @param convID  组的convID
     * @param groupID 组ID
     * @param userID  成员ID
     */
    public void addMember(String convID, final String groupID, String userID) {
        Map<String, Object> attr = SendMessage.setMessAttr(groupID, StaticField.ConvType.twoPerson);
        attr = SendMessage.setSysMessAttr(attr, convID,
                StaticField.SystemMessAttrName.systemFlag.invitation, "");

        List<String> clientIds = new ArrayList<>();
        clientIds.add(AppStaticValue.getUserID());
        clientIds.add(userID);

        Map<String, Object> chatAttr = new HashMap<String, Object>();
        final Map<String, Object> finalAttr = attr;
        AppStaticValue.getImClient().createConversation(clientIds, chatAttr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if (e != null) {
                    if (onResult != null) {
                        onResult.error(e.getMessage());
                    }
                    return;
                }
                SendMessage.getNewInstance().setSendOK(
                        new SendMessage.SendOK() {
                            @Override
                            public void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID) {
                                if (onResult != null) {
                                    onResult.OK();
                                }
                            }

                            @Override
                            public void sendError(String errorMessage, String CONVERSATION_ID) {
                                if (onResult != null) {
                                    onResult.error(errorMessage);
                                }
                            }
                        }
                ).sendTextMessage(avimConversation.getConversationId(),
                        MyUserInfo.getInstance().getUserInfo().getUserName() + "邀请你来加入圈子" +
                                GroupList.getInstance().getGroup(groupID).Name
                        , finalAttr);
            }
        });


    }

    /**
     * 在组类删除用户
     *
     * @param convID  组的convID
     * @param groupID 组ID
     * @param userID  成员ID
     */
    public Observable deleteMember(final String convID, final String groupID, final String userID) {
        final GroupAllInfo[] GroupAllInfo = new GroupAllInfo[1];
        return RetrofitNetwork.retrofit.create(RetrofitAPI.Group.class).queryGroupRX(groupID)
                .subscribeOn(Schedulers.io())
                //LeanCloud 删除
                .flatMap(new Func1<GroupAllInfo, Observable<GroupAllInfo>>() {
                    @Override
                    public Observable<GroupAllInfo> call(final GroupAllInfo groupAllInfo) {
                        return Observable.create(new Observable.OnSubscribe<GroupAllInfo>() {
                            @Override
                            public void call(final Subscriber<? super GroupAllInfo> subscriber) {
                                //LeanCloud删除
                                List<String> userIds = new ArrayList<>();
                                userIds.add(userID);
                                AVIMConversation conversation = AppStaticValue.getImClient().getConversation(convID);
                                conversation.kickMembers(userIds, new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVIMException e) {
                                        if (e != null) {
                                            Log.w(TAG, "LC删除好友失败");
                                            subscriber.onError(new Throwable("LC删除好友失败" + e.getMessage()));
                                        } else {
                                            GroupAllInfo[0] = groupAllInfo;
                                            subscriber.onNext(groupAllInfo);
                                        }
                                    }
                                });
                            }
                        });
                    }
                })
                //后台删除
                .flatMap(new Func1<GroupAllInfo, Observable<GroupAllInfo>>() {
                    @Override
                    public Observable<GroupAllInfo> call(final GroupAllInfo groupAllInfo) {
                        return Observable.create(new Observable.OnSubscribe<GroupAllInfo>() {
                            @Override
                            public void call(final Subscriber<? super GroupAllInfo> subscriber) {
                                //后台删除
                                UserManageInGroup.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                                    @Override
                                    public void onOK(Object ts) {
                                        subscriber.onNext(groupAllInfo);
                                    }

                                    @Override
                                    public void onError(String Message) {
                                        subscriber.onError(new Throwable("后台删除好友失败:" + Message));
                                    }
                                }).deleteUser(groupID, userID);
                            }
                        });
                    }
                })
                //通知,返回发通知的ConvID
                .flatMap(new Func1<GroupAllInfo, Observable<String>>() {
                             @Override
                             public Observable<String> call(final GroupAllInfo groupAllInfo) {
                                 return Observable.create(new Observable.OnSubscribe<String>() {
                                     @Override
                                     public void call(final Subscriber<? super String> subscriber) {
                                         if (groupAllInfo.group.createUser.compareTo(AppStaticValue.getUserID())
                                                 == 0) {
                                             StartPrivateChat.getNewInstance(new StartPrivateChat.GetConvID() {
                                                 @Override
                                                 public void onConvID(String convID) {
                                                     subscriber.onNext(convID);

                                                 }

                                                 @Override
                                                 public void onError(String errorMessage) {
                                                     subscriber.onError(new Throwable(errorMessage));
                                                 }
                                             }).getPrivateChatConvID(userID);
                                         } else {
                                             subscriber.onNext(convID);
                                         }
                                     }
                                 });
                             }
                         }

                )
                .flatMap(new Func1<String, Observable<SendMessage.RXSendOK>>() {
                    @Override
                    public Observable<SendMessage.RXSendOK> call(String SendConvID) {
                        Map<String, Object> attr = SendMessage.setMessAttr(groupID,
                                StaticField.ConvType.twoPerson);
                        attr = SendMessage.setSysMessAttr(attr, convID,
                                StaticField.SystemMessAttrName.systemFlag.kicked, "");
                        if (GroupAllInfo[0].group.createUser.equals(AppStaticValue.getUserID())) {
                            return SendMessage.getNewInstance().sendRXTextMessage(SendConvID,
                                    String.format("你被[ %s ]踢出了群[ %s ]OAQ",
                                            MyUserInfo.getInstance().getUserInfo().getUserName(),
                                            GroupAllInfo[0].group.groupName), attr);
                        } else {
                            return SendMessage.getNewInstance().sendRXTextMessage(convID,
                                    String.format("[ %s ]退出了圈子[ %s ]", MyUserInfo.getInstance().getUserInfo().getUserName(), GroupAllInfo[0].group.groupName), attr);
                        }


                    }
                });
    }

    /**
     * 删除组
     *
     * @param convID  组的convID
     * @param groupID 组ID
     */
    public void deleteGroup(String convID, String groupID) {
        //后台
        UserManageInGroup.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                if (onResult != null) {
                    onResult.OK();
                }
            }

            @Override
            public void onError(String Message) {
                if (onResult != null) {
                    onResult.error("后台删除好友失败:" + Message);
                }
            }
        }).deleteGroup(groupID);

        //LC
        Map<String, Object> attr = SendMessage.setMessAttr(groupID, StaticField.ConvType.GROUP);
        attr = SendMessage.setSysMessAttr(attr, convID,
                StaticField.SystemMessAttrName.systemFlag.group_delete, "圈子已解散");
        SendMessage.getNewInstance().setSendOK(
                new SendMessage.SendOK() {
                    @Override
                    public void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID) {
                        Log.i(TAG, "LC 发送成功");
                    }

                    @Override
                    public void sendError(String errorMessage, String CONVERSATION_ID) {
                        Log.w(TAG, errorMessage);
                        if (onResult != null) {
                            onResult.error(errorMessage);
                        }
                    }
                }
        ).sendTextMessage(convID, "", attr);
    }

    /**
     * 同意加入组
     *
     * @param isAccept 是否接受加入
     * @param convID   组的convID
     * @param groupID  组ID
     */
    public void acceptToJoinGroup(boolean isAccept, final String convID, final String groupID) {
        if (!isAccept) {
            return;
        }
        //后台
        UserManageInGroup.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                GroupInviteAns groupInviteAns = (GroupInviteAns) ts;
                GroupClass groupClass = GroupClass.getGroupByGroupInviteAns(groupInviteAns);
                GroupList.getInstance().addGroup(groupClass);
            }

            @Override
            public void onError(String Message) {
                if (onResult != null) {
                    onResult.error("后台失败" + Message);
                }
            }
        }).acceptJoinGroup(groupID, AppStaticValue.getUserID());

        //LC
        List<String> userIds = new ArrayList<>();
        userIds.add(AppStaticValue.getUserID());
        AppStaticValue.getImClient().getConversation(convID).addMembers(userIds, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    if (onResult != null) {
                        onResult.OK();
                    }
                } else {
                    Log.w(TAG, "LC addMembers err:" + e.getMessage());
                    if (onResult != null) {
                        onResult.error("LC addMembers err:" + e.getMessage());
                    }
                }
            }
        });

        //碰撞圈子
        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Theme theme = (Theme) ts;
                for (final Theme.ActsEntity actsEntity : theme.acts) {
                    ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            BoomGroup boomGroup = (BoomGroup) ts;
                            for (BoomGroup.GroupmatchEntity groupmatch : boomGroup.groupmatch) {
                                BoomGroupList.getInstance().addGroup(
                                        BoomGroupClass.getBoomGroupFromBoomGroupGson(groupmatch, actsEntity.id)
                                );
                            }
                        }

                        @Override
                        public void onError(String Message) {

                        }
                    }).getBoomGroup(actsEntity.id, groupID);
                }
            }

            @Override
            public void onError(String Message) {

            }
        }).getThemes();

    }

    public interface OnResult {
        void OK();

        void error(String errorMessage);
    }

}
