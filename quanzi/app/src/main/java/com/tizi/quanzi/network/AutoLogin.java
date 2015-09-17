package com.tizi.quanzi.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.tizi.quanzi.Intent.StartMainActivity;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;

/**
 * Created by qixingchen on 15/7/23.
 * 登陆
 * todo 获取本地ts与服务器的差
 */
public class AutoLogin extends RetrofitNetworkAbs {
    private static SharedPreferences preferences;

    private RetrofitAPI.UserAccount userAccountService = RetrofitNetwork.retrofit.create(RetrofitAPI.UserAccount.class);

    public static AutoLogin getNewInstance() {
        return new AutoLogin();
    }

    private AutoLogin() {
        preferences = App.getApplication().getSharedPreferences(
                StaticField.TokenPreferences.TOKENFILE, Context.MODE_PRIVATE);
    }

    /**
     * 从 本地偏好加载密码登陆
     *
     * @return 是否有密码
     */
    public boolean loginFromPrefer() {
        String password = preferences.getString(StaticField.TokenPreferences.PASSWORD, "");
        assert password != null;
        if (password.compareTo("") == 0) {
            return false;
        }
        loginFromPrePassword(password);
        return true;
    }

    /**
     * 使用预加密的密码登陆
     * 并储存密码
     * 账号来自  App.getUserPhone()
     *
     * @see GetPassword
     */
    public void loginFromPrePassword(String PrePassword) {
        preferences.edit().putString(StaticField.TokenPreferences.PASSWORD, PrePassword).apply();
        String RawPassword;
        // TODO: 15/9/17 delete test account
        if (AppStaticValue.getUserPhone().compareTo("1") == 0) {
            RawPassword = "96e79218965eb72c92a549dd5a330112";
        } else if (AppStaticValue.getUserPhone().compareTo("2") == 0) {
            RawPassword = "e3ceb5881a0a1fdaad01296d7554868d";
        } else {
            RawPassword = GetPassword.LaterHASH(PrePassword);
        }

        String account = AppStaticValue.getUserPhone();

        loginRaw(account, RawPassword);
    }

    /**
     * 使用给予的账号密码登陆
     */
    public void loginRaw(String account, String password) {

        userAccountService.login(account, password, Tool.getSignMap()).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(retrofit.Response<Login> response) {
                if (myOnResponse(response)) {
                    Login login = response.body();
                    setUserInfo(AppStaticValue.getUserPhone(), login.getUser().getId(), login.getUser().getToken());
                    MyUserInfo.getInstance().setUserInfo(login.getUser());
                    PrivateMessPairList.getInstance().getGroupsFromDataBase();
                    StartMainActivity.startByLoginGroup(login.getGroup(), App.getApplication());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });

    }

    /**
     * 设置用户信息（APP的更新）
     *
     * @param phone 用户手机号
     * @param ID    userID
     * @param Token userToken
     */
    public static void setUserInfo(String phone, String ID, String Token) {
        AppStaticValue.setUserToken(Token);
        AppStaticValue.setUserID(ID);
        AppStaticValue.setUserPhone(phone);
        AppStaticValue.setDataBaseHelper(ID);
        AppStaticValue.getNewImClient(ID);
    }


    @SuppressWarnings("unchecked")
    @Override
    public AutoLogin setNetworkListener(NetworkListener networkListener) {
        return this.setNetworkListener(networkListener, this);
    }

}
