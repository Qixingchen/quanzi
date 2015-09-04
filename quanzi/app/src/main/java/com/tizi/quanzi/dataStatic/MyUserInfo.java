package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.gson.Login;

/**
 * Created by qixingchen on 15/9/3.
 * 用户的个人信息 {@link com.tizi.quanzi.gson.Login.UserEntity}
 */
public class MyUserInfo {
    private Login.UserEntity userInfo;
    private static MyUserInfo mInstance;

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

    private MyUserInfo() {
    }

    public Login.UserEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Login.UserEntity userInfo) {
        this.userInfo = userInfo;
    }

}
