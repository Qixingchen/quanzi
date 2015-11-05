package com.tizi.quanzi.dataStatic;

import android.support.annotation.Nullable;

import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.network.LoginAndUserAccount;

/**
 * Created by qixingchen on 15/9/3.
 * 用户的个人信息 {@link com.tizi.quanzi.gson.Login.UserEntity}
 */
public class MyUserInfo {
    private static MyUserInfo mInstance;
    private Login.UserEntity userInfo;

    private MyUserInfo() {
    }

    public static MyUserInfo getInstance() {
        if (mInstance == null) {
            synchronized (MyUserInfo.class) {
                if (mInstance == null) {
                    mInstance = new MyUserInfo();
                }
            }
        }
        return mInstance;
    }

    @Nullable
    public Login.UserEntity getUserInfo() {
        if (userInfo == null || userInfo.getIcon() == null) {
            LoginAndUserAccount.getNewInstance().loginFromPrefer();
        }
        return userInfo;
    }

    public void setUserInfo(Login.UserEntity userInfo) {
        this.userInfo = userInfo;
    }

}
