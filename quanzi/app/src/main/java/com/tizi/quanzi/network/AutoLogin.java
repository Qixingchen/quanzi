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
     * @return 登陆是否成功
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
     * 账号来自  App.getUserPhone()
     *
     * @see GetPassword
     */
    public void loginFromPrePassword(String password) {
        Map<String, String> loginPara = new TreeMap<>();
        loginPara.put("account", App.getUserPhone());
        if (App.getUserPhone().compareTo("1") == 0) {
            loginPara.put("password", "96e79218965eb72c92a549dd5a330112");
        } else if (App.getUserPhone().compareTo("2") == 0) {
            loginPara.put("password", "e3ceb5881a0a1fdaad01296d7554868d");
        } else {
            loginPara.put("password", GetPassword.LaterHASH(password));
        }
        preferences.edit().putString(StaticField.TokenPreferences.PASSWORD, password).apply();
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
                    setUserInfo(App.getUserPhone(), login.getUser().getId(), login.getUser().getToken());
                    StartMainActivity.startByLoginGroup(login.getGroup(), mContext);
                } else {
                    Toast.makeText(mContext, login.getMsg(), Toast.LENGTH_LONG).show();
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
        App.setUserToken(Token);
        App.setUserID(ID);
        App.setUserPhone(phone);
        App.setDataBaseHelper(ID);
        App.getNewImClient(ID);
        return mInstance;
    }
// TODO: 15/8/20 更换为回调接口

    /**
     * 设置成功监听器
     *
     * @param mOKListener Response.Listener
     */
    @Deprecated
    public AutoLogin setmOKListener(Response.Listener<String> mOKListener) {
        AutoLogin.mOKListener = mOKListener;
        return mInstance;
    }

    /**
     * 设置失败监听器
     *
     * @param mErrorListener Response.ErrorListener
     */
    @Deprecated
    public AutoLogin setmErrorListener(Response.ErrorListener mErrorListener) {
        AutoLogin.mErrorListener = mErrorListener;
        return mInstance;
    }
}
