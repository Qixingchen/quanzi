package com.tizi.quanzi.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/9/9.
 * 群设置管理－后台
 */
public class GroupSetting extends NetWorkAbs {
    private static GroupSetting mInstance;

    public static GroupSetting getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            synchronized (GroupSetting.class) {
                if (mInstance == null) {
                    mInstance = new GroupSetting();
                }
            }
        }
        return (GroupSetting) mInstance;
    }

    private GroupSetting() {
        gson = new Gson();
        mOKListener = makeOkListener();
        mErrorListener = makeErrorListener();
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
                    Log.e(TAG, onlySuccess.getMsg());
                    if (networkListener != null) {
                        networkListener.onError(onlySuccess.getMsg());
                    }
                }
            }
        };
        return OKListener;
    }

    private void changeField(String groupID, String field, String value) {
        Map<String, String> para = new TreeMap<>();
        para.put("groupid", groupID);
        para.put("field", field);
        para.put("value", value);


        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/group/updateFieldF", para);
    }

    public void ChangeName(String groupID, String name) {
        changeField(groupID, "groupName", name);
    }

    public void changeIcon(String groupID, String uri) {
        changeField(groupID, "icon", uri);
    }
}
