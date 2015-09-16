package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;

/**
 * Created by qixingchen on 15/9/3.
 * 通过account查询用户
 */
public class FindUser extends RetrofitNetworkAbs {

    public static FindUser getNewInstance() {
        return new FindUser();
    }

    private RetrofitAPI.FindUser findUserService = RetrofitNetwork.retrofit.create(RetrofitAPI.FindUser.class);

    /**
     * 查找用户
     */
    public FindUser finduser(String account) {

        findUserService.getUserByAccount(account, Tool.getSignMap()).enqueue(new Callback<OtherUserInfo>() {
            @Override
            public void onResponse(retrofit.Response<OtherUserInfo> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FindUser setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
