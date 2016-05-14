package com.tizi.quanzi.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.StaticField;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qixingchen on 15/7/23.
 * 登陆
 *
 * @see com.tizi.quanzi.network.RetrofitAPI.UserAccount
 */
public class LoginAndUserAccount extends RetrofitNetworkAbs {
    private static SharedPreferences preferences;

    private RetrofitAPI.UserAccount userAccountService = RetrofitNetwork.retrofit.create(RetrofitAPI.UserAccount.class);

    private LoginAndUserAccount() {
        preferences = App.getApplication().getSharedPreferences(
                StaticField.Preferences.TOKENFILE, Context.MODE_PRIVATE);
    }

    public static LoginAndUserAccount getNewInstance() {
        return new LoginAndUserAccount();
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
     * 使用无加密的密码登陆
     * 并储存密码
     * 账号来自  App.getUserPhone()
     *
     * @see GetPassword
     */
    public void loginNoHash(String password) {
        loginFromPrePassword(GetPassword.preHASH(password));
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
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() && response.body().success) {
                    Login login = response.body();
                    setUserInfo(AppStaticValue.getUserPhone(), login.getUser().getId(), login.getUser().getToken());
                    MyUserInfo.getInstance().setUserInfo(login.getUser());
                    PrivateMessPairList.getInstance().getGroupsFromDataBase();
                    SystemMessageList.getInstance().getGroupsFromDataBase();
                    ((GroupList) (GroupList.getInstance())).setGroupListByLoginGroup(login.getGroup());
                }
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                myOnFailure(t);
            }
        });

    }

    /**
     * 更改用户密码
     */
    public void changePassword(String account, String password) {
        AppStaticValue.setUserID(account);
        AppStaticValue.setUserToken(account);
        password = GetPassword.fullHash(password);
        userAccountService.changePassword(account, password).enqueue(new Callback<OnlySuccess>() {
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
    public LoginAndUserAccount setNetworkListener(NetworkListener networkListener) {
        return this.setNetworkListener(networkListener, this);
    }

}
