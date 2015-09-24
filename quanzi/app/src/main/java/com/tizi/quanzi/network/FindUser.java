package com.tizi.quanzi.network;

import com.google.gson.Gson;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.ReadContact;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.main.MainActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;

/**
 * Created by qixingchen on 15/9/3.
 * 通过account查询用户
 *
 * @see com.tizi.quanzi.network.RetrofitAPI.FindUser
 */
public class FindUser extends RetrofitNetworkAbs {

    private RetrofitAPI.FindUser findUserService = RetrofitNetwork.retrofit.create(RetrofitAPI.FindUser.class);

    public static FindUser getNewInstance() {
        return new FindUser();
    }

    /**
     * 查找用户
     */
    public void finduserByAccount(String account) {
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

    }

    public void findUserByID(String ID) {
        findUserService.getUserByID(ID, Tool.getSignMap()).enqueue(new Callback<OtherUserInfo>() {
            @Override
            public void onResponse(retrofit.Response<OtherUserInfo> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    public void findContactUsers() {
        // TODO: 15/9/18 activity
        List<ReadContact.Mobiles> mobilesList = ReadContact.readContact(
                AppStaticValue.getActivity(MainActivity.class.getSimpleName()));

        String mobileString = new Gson().toJson(mobilesList);
        Log.i(TAG, mobileString);
        findUserService.findContactUser(Tool.getUTF_8String(mobileString),
                Tool.getSignMap()).enqueue(new Callback<ContantUsers>() {
            @Override
            public void onResponse(Response<ContantUsers> response) {
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
    public FindUser setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
