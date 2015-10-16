package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.GroupInviteAns;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/9/2.
 * 成员管理后台调用
 *
 * @see com.tizi.quanzi.network.RetrofitAPI.GroupMember
 */
public class UserManageInGroup extends RetrofitNetworkAbs {


    private RetrofitAPI.GroupMember groupMemberSer = RetrofitNetwork.retrofit.create(RetrofitAPI.GroupMember.class);

    public static UserManageInGroup getNewInstance() {
        return new UserManageInGroup();
    }

    /**
     * 将用户从圈子中删除
     */
    public void deleteUser(String GroupID, String UserID) {

        groupMemberSer.exitOrDeleteMember(GroupID, UserID, Tool.getSignMap()).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(retrofit.Response<OnlySuccess> response, Retrofit retrofit) {
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

        groupMemberSer.acceptGroupInvite(GroupID, UserID).enqueue(new Callback<GroupInviteAns>() {
            @Override
            public void onResponse(retrofit.Response<GroupInviteAns> response, Retrofit retrofit) {
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
            public void onResponse(Response<OnlySuccess> response, Retrofit retrofit) {
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
