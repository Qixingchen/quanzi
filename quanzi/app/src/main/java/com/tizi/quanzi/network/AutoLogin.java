package com.tizi.quanzi.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.StaticField;

import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/7/23.
 * 登陆
 *
 * @see com.tizi.quanzi.network.RetrofitAPI.UserAccount
 */
public class AutoLogin extends RetrofitNetworkAbs {
    private static SharedPreferences preferences;

    private RetrofitAPI.UserAccount userAccountService = RetrofitNetwork.retrofit.create(RetrofitAPI.UserAccount.class);

    private AutoLogin() {
        preferences = App.getApplication().getSharedPreferences(
                StaticField.Preferences.TOKENFILE, Context.MODE_PRIVATE);
    }

    public static AutoLogin getNewInstance() {
        return new AutoLogin();
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
        DBAct.flushDBID();
        AddNotification.getInstance().setSharedPreferences();
    }

    /**
     * 从 本地偏好加载密码登陆
     *
     * @return 是否有密码
     */
    public boolean loginFromPrefer() {
        String password = preferences.getString(StaticField.Preferences.PASSWORD, "");
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
        preferences.edit().putString(StaticField.Preferences.PASSWORD, PrePassword).apply();
        String RawPassword = GetPassword.LaterHASH(PrePassword);
        String account = AppStaticValue.getUserPhone();

        loginRaw(account, RawPassword);
    }

    /**
     * 使用给予的账号密码登陆
     */
    public void loginRaw(String account, String password) {

        userAccountService.login(account, password).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(retrofit.Response<Login> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body().success) {
                    Login login = response.body();
                    setUserInfo(AppStaticValue.getUserPhone(), login.getUser().getId(), login.getUser().getToken());
                    MyUserInfo.getInstance().setUserInfo(login.getUser());
                    PrivateMessPairList.getInstance().getGroupsFromDataBase();
                    ((GroupList) (GroupList.getInstance())).setGroupListByLoginGroup(login.getGroup());
                }
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
    public AutoLogin setNetworkListener(NetworkListener networkListener) {
        return this.setNetworkListener(networkListener, this);
    }

}
