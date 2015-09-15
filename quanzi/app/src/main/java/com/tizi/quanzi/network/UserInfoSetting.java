package com.tizi.quanzi.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/9/10.
 * 用户个人信息变更
 */
public class UserInfoSetting extends NetWorkAbs {
    private static UserInfoSetting mInstance;

    public static UserInfoSetting getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UserInfoSetting.class) {
                if (mInstance == null) {
                    mInstance = new UserInfoSetting();
                }
            }
        }
        return mInstance;
    }

    private UserInfoSetting() {
        gson = new Gson();
        mOKListener = makeOkListener();
        mErrorListener = makeErrorListener();
    }

    private void changeFiled(String field, String value) {
        Map<String, String> para = new TreeMap<>();
        para.put("field", field);
        para.put("value", value);
        para.put("userid", AppStaticValue.getUserID());


        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/user/updateFieldF", para);
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

    @Override
    protected Response.Listener<String> makeOkListener() {
        Response.Listener<String> OKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                OnlySuccess onlySuccess = gson.fromJson(response, OnlySuccess.class);
                if (onlySuccess.isSuccess()) {
                    if (networkListener != null) {
                        networkListener.onOK(null);
                    }
                } else {
                    Log.e(TAG, "Error");
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    networkListener.onError("");
                }
            }
        };
        return OKListener;
    }
}
