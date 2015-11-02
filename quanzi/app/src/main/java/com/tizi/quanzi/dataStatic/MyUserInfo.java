package com.tizi.quanzi.dataStatic;

import android.content.Intent;

import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.ui.login.LoginActivity;

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

    public Login.UserEntity getUserInfo() {
        if (userInfo == null || userInfo.getIcon() == null) {
            Intent log_in = new Intent(App.getApplication(), LoginActivity.class);
            log_in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            App.getApplication().startActivity(log_in);
        }
        return userInfo;
    }

    public void setUserInfo(Login.UserEntity userInfo) {
        this.userInfo = userInfo;
    }

}
