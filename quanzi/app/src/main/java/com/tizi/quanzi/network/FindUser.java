package com.tizi.quanzi.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/9/3.
 * 通过account查询用户
 */
public class FindUser {
    private static FindUser mInstance;
    private static final String TAG = FindUser.class.getSimpleName();
    private Gson gson;
    private Context mContext;

    private FindUserListener findUserListener;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    public FindUser setFindUserListener(FindUserListener findUserListener) {
        this.findUserListener = findUserListener;
        return mInstance;
    }

    public static FindUser getInstance() {
        if (mInstance == null) {
            synchronized (FindUser.class) {
                if (mInstance == null) {
                    mInstance = new FindUser();
                }
            }
        }
        return mInstance;
    }

    private FindUser() {
        this.mContext = App.getApplication();
        gson = new Gson();
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO: 15/9/3 add json
                OtherUserInfo otherUserInfo = gson.fromJson(response, OtherUserInfo.class);
                if (otherUserInfo.success) {
                    if (findUserListener != null) {
                        findUserListener.onOK(otherUserInfo);
                    }
                } else {
                    Log.e(TAG, "Error");
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    findUserListener.onError("Error");
                }
            }
        };
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                if (findUserListener != null) {
                    findUserListener.onError(error.getMessage());
                }
            }
        };
    }

    /**
     * 将用户加入圈子
     */
    public FindUser finduser(String account) {

        Map<String, String> findUserPara = new TreeMap<>();
        findUserPara.put("account", account);

        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/user/findUserF", findUserPara);
        return mInstance;
    }

    public interface FindUserListener {
        /**
         * 成功回调
         */
        void onOK(OtherUserInfo otherUserInfo);

        void onError(String errorMessage);
    }
}
