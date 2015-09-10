package com.tizi.quanzi.network;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/8/20.
 * 网络请求抽象类 未完成
 * 需要实现 public static XXXX getInstance(Context context)
 * 创造函数
 * private XXXX() {
 * gson = new Gson();
 * mOKListener = makeOkListener();
 * mErrorListener = makeErrorListener();
 * }
 * 请求具体
 */
public abstract class NetWorkAbs {
    protected static Context mContext;
    protected Gson gson;

    protected static Response.Listener<String> mOKListener;
    protected static Response.ErrorListener mErrorListener;
    protected final String TAG = this.getClass().getSimpleName();
    protected NetworkListener networkListener;

    protected static NetWorkAbs mInstance;

    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    protected Response.ErrorListener makeErrorListener() {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                if (networkListener != null) {
                    networkListener.onError(error.getMessage());
                }
            }
        };
        return errorListener;
    }

    protected abstract Response.Listener<String> makeOkListener();

    public interface NetworkListener {
        // TODO: 15/8/20 why
        void onOK(Object ts);

        void onError(String Message);
    }
}
