package com.tizi.quanzi.network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
 */
public class AutoLogin {
    private static Context mContext;
    private static SharedPreferences preferences;
    private static AutoLogin mInstance;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    public static AutoLogin getInstance(Context mContext) {
        if (mInstance == null) {
            synchronized (AutoLogin.class) {
                mInstance = new AutoLogin(mContext);
            }
        }
        return mInstance;
    }

    private AutoLogin(Context mContext) {
        AutoLogin.mContext = mContext;
        preferences = mContext.getSharedPreferences(
                StaticField.TokenPreferences.TOKENFILE, Context.MODE_PRIVATE);
    }

    public boolean login() {
        String password = preferences.getString(StaticField.TokenPreferences.PASSWORD, "");
        assert password != null;
        if (password.compareTo("") == 0) {
            return false;
        }
        login(password);
        return true;
    }

    public void login(String password) {
        Map<String, String> loginPara = new TreeMap<>();
        loginPara.put("account", App.getUserPhone());
        loginPara.put("password", GetPassword.LaterHASH(password));
        preferences.edit().putString(StaticField.TokenPreferences.PASSWORD, password).apply();
        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/applogin/loginF", loginPara);
    }

    public AutoLogin makeErrorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("自动登录", error.getMessage());
            }
        };
        return mInstance;
    }

    public AutoLogin makeOKListener() {
        AutoLogin.mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Login login = gson.fromJson(response, Login.class);
                App.setUserToken(login.getUser().getToken());
                App.setUserID(login.getUser().getId());
                StartMainActivity.startByLoginGroup(login.getGroup(), mContext);
            }
        };
        return mInstance;
    }

    public AutoLogin setmOKListener(Response.Listener<String> mOKListener) {
        AutoLogin.mOKListener = mOKListener;
        return mInstance;
    }

    public AutoLogin setmErrorListener(Response.ErrorListener mErrorListener) {
        AutoLogin.mErrorListener = mErrorListener;
        return mInstance;
    }
}
