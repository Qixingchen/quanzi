package com.tizi.quanzi.network;

import android.content.Context;

import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/9/16.
 * RetrofitNetwork 抽象类
 */
public abstract class RetrofitNetworkAbs {

    protected Context mContext;
    protected final String TAG = this.getClass().getSimpleName();
    protected NetworkListener networkListener;

    protected <T extends RetrofitNetworkAbs> T setNetworkListener(NetworkListener networkListener,
                                                                  T type) {
        type.networkListener = networkListener;
        return type;
    }

    public abstract <T extends RetrofitNetworkAbs> T setNetworkListener(NetworkListener networkListener);

    public interface NetworkListener {
        // TODO: 15/8/20 why
        void onOK(Object ts);

        void onError(String Message);
    }

    /**
     * 检查Response是否成功
     *
     * @param response retrofit 的 response
     */
    protected void myOnResponse(retrofit.Response<? extends OnlySuccess> response) {
        if (response.isSuccess() && response.body().success) {
            Log.i(TAG, "success");
            if (networkListener != null) {
                networkListener.onOK(response.body());
            }
        } else {
            String mess = response.isSuccess() ? response.body().msg : response.message();
            Log.w(TAG, mess);
            if (networkListener != null) {
                networkListener.onError(mess);
            }
        }
    }


    /**
     * OnFailure
     */
    protected void myOnFailure(Throwable t) {
        Log.w(TAG, t.getMessage());
        if (networkListener != null) {
            networkListener.onError(t.getMessage());
        }
    }
}
