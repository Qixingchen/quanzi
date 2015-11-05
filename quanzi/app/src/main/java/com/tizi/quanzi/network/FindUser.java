package com.tizi.quanzi.network;

import com.google.gson.Gson;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.ReadContact;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
        findUserService.getUserByAccount(account).enqueue(new Callback<OtherUserInfo>() {
            @Override
            public void onResponse(retrofit.Response<OtherUserInfo> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });

    }

    public void findUserByID(String ID) {
        findUserService.getUserByID(ID).enqueue(new Callback<OtherUserInfo>() {
            @Override
            public void onResponse(retrofit.Response<OtherUserInfo> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    public void findContactUsers() {
        List<ReadContact.Mobiles> mobilesList = ReadContact.readContact(App.getApplication());

        //每次只传 ContactLimit 个
        int contactLimit = StaticField.QueryLimit.ContactLimit;
        final int times = (mobilesList.size() - 1) / contactLimit + 1;
        String[] mobileString = new String[times];
        final int[] ansTimes = {0};
        final List<ContantUsers.MobilesEntity> mobiles = new ArrayList<>();
        //分批次
        for (int i = 0; i < times; i++) {
            mobileString[i] = new Gson().toJson(mobilesList.subList(i * contactLimit,
                    Math.min((i + 1) * contactLimit - 1, mobilesList.size())));
            Log.i(TAG, mobileString[i] + "\n \n");
            findUserService.findContactUser(Tool.getUTF_8String(mobileString[i]))
                    .enqueue(new Callback<ContantUsers>() {
                        @Override
                        public void onResponse(Response<ContantUsers> response, Retrofit retrofit) {
                            if (response.isSuccess() && response.body().success) {
                                Log.i(TAG, "success");
                                ansTimes[0]++;
                                mobiles.addAll(response.body().mobiles);
                                if (ansTimes[0] == times) {
                                    response.body().mobiles = mobiles;
                                    findContactUsersComplete(response);
                                }
                            } else {
                                String mess = response.isSuccess() ? response.body().msg : response.message();
                                Log.w(TAG, mess);
                                if (networkListener != null) {
                                    networkListener.onError(mess);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            myOnFailure(t);
                        }
                    });
        }
    }

    private void findContactUsersComplete(Response<ContantUsers> response) {
        myOnResponse(response);
    }

    @SuppressWarnings("unchecked")
    @Override
    public FindUser setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
