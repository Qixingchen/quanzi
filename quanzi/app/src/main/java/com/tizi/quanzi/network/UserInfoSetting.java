package com.tizi.quanzi.network;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;

/**
 * Created by qixingchen on 15/9/10.
 * 用户个人信息变更
 */
public class UserInfoSetting extends RetrofitNetworkAbs {

    public static UserInfoSetting getNewInstance() {
        return new UserInfoSetting();
    }

    private RetrofitAPI.UserAccount userAccountSer = RetrofitNetwork.retrofit.create(RetrofitAPI.UserAccount.class);

    private void changeFiled(String field, String value) {

        value = Tool.getUTF_8String(value);
        userAccountSer.changeUserInfo(field, value, AppStaticValue.getUserID(),
                Tool.getSignMap()).enqueue(new Callback<OnlySuccess>() {
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


    public void changeName(String name) {
        changeFiled("userName", name);
    }

    public void changeSex(String sex) {
        changeFiled("sex", sex);
    }

    public void changeFace(String uri) {
        changeFiled("icon", uri);
    }

    public void changeSign(String sign) {
        changeFiled("signature", sign);
    }

    public void changeArea(String area) {
        changeFiled("area", area);
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserInfoSetting setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
