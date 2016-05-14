package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.GroupInviteAns;
import com.tizi.quanzi.gson.OnlySuccess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        groupMemberSer.exitOrDeleteMember(GroupID, UserID).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Call<OnlySuccess> call, Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<OnlySuccess> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 同意加入圈子
     */
    public void acceptJoinGroup(String GroupID, String UserID, String convID) {

        groupMemberSer.acceptGroupInvite(GroupID, UserID, convID).enqueue(new Callback<GroupInviteAns>() {
            @Override
            public void onResponse(Call<GroupInviteAns> call, Response<GroupInviteAns> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<GroupInviteAns> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 删除圈子
     */
    public void deleteGroup(String GroupID) {

        groupMemberSer.dropGroup(GroupID).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Call<OnlySuccess> call, Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<OnlySuccess> call, Throwable t) {
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
