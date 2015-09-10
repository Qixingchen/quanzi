package com.tizi.quanzi.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.Intent.StartMainActivity;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.StaticField;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/7/23.
 * 登陆
 * todo 获取本地ts与服务器的差
 */
public class AutoLogin {
    private static Context mContext;
    private static SharedPreferences preferences;
    private static AutoLogin mInstance;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    private static onLogin onLogin;

    public static AutoLogin getInstance() {
        if (mInstance == null) {
            synchronized (AutoLogin.class) {
                if (mInstance == null) {
                    mInstance = new AutoLogin(App.getApplication());
                }
            }
        }
        return mInstance;
    }

    private AutoLogin(Context mContext) {
        AutoLogin.mContext = mContext;
        preferences = mContext.getSharedPreferences(
                StaticField.TokenPreferences.TOKENFILE, Context.MODE_PRIVATE);
        makeErrorListener();
        makeOKListener();
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
        Map<String, String> loginPara = new TreeMap<>();
        loginPara.put("account", account);
        loginPara.put("password", password);
        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/applogin/loginF", loginPara);
    }


    private AutoLogin makeErrorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("自动登录", error.getMessage());
                if (onLogin != null) {
                    onLogin.error(error.getMessage());
                }
            }
        };
        return mInstance;
    }

    private AutoLogin makeOKListener() {
        AutoLogin.mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Login login = gson.fromJson(response, Login.class);
                if (login.isSuccess()) {
                    setUserInfo(AppStaticValue.getUserPhone(), login.getUser().getId(), login.getUser().getToken());
                    MyUserInfo.getInstance().setUserInfo(login.getUser());
                    PrivateMessPairList.getInstance().getGroupsFromDataBase();
                    StartMainActivity.startByLoginGroup(login.getGroup(), mContext);
                    if (onLogin != null) {
                        onLogin.Success(login);
                    }
                } else {
                    Toast.makeText(mContext, login.getMsg(), Toast.LENGTH_LONG).show();
                    if (onLogin != null) {
                        onLogin.error(login.getMsg());
                    }
                }
            }
        };
        return mInstance;
    }

    /**
     * 设置用户信息（APP的更新）
     *
     * @param phone 用户手机号
     * @param ID    userID
     * @param Token userToken
     */
    public static AutoLogin setUserInfo(String phone, String ID, String Token) {
        AppStaticValue.setUserToken(Token);
        AppStaticValue.setUserID(ID);
        AppStaticValue.setUserPhone(phone);
        AppStaticValue.setDataBaseHelper(ID);
        AppStaticValue.getNewImClient(ID);
        return mInstance;
    }

    public AutoLogin setOnLogin(AutoLogin.onLogin onLogin) {
        AutoLogin.onLogin = onLogin;
        return mInstance;
    }

    public interface onLogin {
        void Success(Login login);

        void error(String errorMess);
    }
}
