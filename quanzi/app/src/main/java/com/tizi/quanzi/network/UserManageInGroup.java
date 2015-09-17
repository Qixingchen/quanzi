package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;
import retrofit.Response;

/**
 * Created by qixingchen on 15/9/2.
 * 成员管理后台调用
 */
public class UserManageInGroup extends RetrofitNetworkAbs {


    public static UserManageInGroup getNewInstance() {
        return new UserManageInGroup();
    }

    private RetrofitAPI.GroupMember groupMemberSer = RetrofitNetwork.retrofit.create(RetrofitAPI.GroupMember.class);

    /**
     * 将用户从圈子中删除
     */
    public void deleteUser(String GroupID, String UserID) {

        groupMemberSer.exitOrDeleteMember(GroupID, UserID, Tool.getSignMap()).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(retrofit.Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 同意加入圈子
     */
    public void acceptJoinGroup(String GroupID, String UserID) {

        groupMemberSer.acceptGroupInvite(GroupID, Tool.getSignMap()).enqueue(new Callback<GroupAllInfo>() {
            @Override
            public void onResponse(retrofit.Response<GroupAllInfo> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 删除圈子
     */
    public void deleteGroup(String GroupID) {

        groupMemberSer.dropGroup(GroupID, Tool.getSignMap()).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserManageInGroup setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }


}
