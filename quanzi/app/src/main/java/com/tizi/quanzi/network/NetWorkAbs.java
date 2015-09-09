package com.tizi.quanzi.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/8/20.
 * 网络请求抽象类 未完成
 */
public abstract class NetWorkAbs {
    private static Context mContext;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;
    private final String TAG = this.getClass().getSimpleName();
    private NetworkListener networkListener;

    protected static NetWorkAbs mInstance;

    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    private NetWorkAbs makeErrorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        return mInstance;
    }

    public abstract NetWorkAbs makeOKListener();

    public interface NetworkListener {
        // TODO: 15/8/20 why
        void onOK(Object ts);

        void onError();
    }
}
