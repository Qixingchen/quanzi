package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/9/16.
 * RetrofitNetwork 抽象类
 */
public abstract class RetrofitNetworkAbs {

    protected final String TAG = this.getClass().getSimpleName();
    protected NetworkListener networkListener;

    protected <T extends RetrofitNetworkAbs> T setNetworkListener(NetworkListener networkListener,
                                                                  T type) {
        type.networkListener = networkListener;
        return type;
    }

    public abstract <T extends RetrofitNetworkAbs> T setNetworkListener(NetworkListener networkListener);

    // TODO: 15/9/17 T 抽象
    public interface NetworkListener<T extends Object> {
        // TODO: 15/8/20 why
        void onOK(T ts);

        void onError(String Message);
    }

    /**
     * 检查Response是否成功
     *
     * @param response retrofit 的 response
     *
     * @return 是否成功
     */
    protected boolean myOnResponse(retrofit.Response<? extends OnlySuccess> response) {
        if (response.isSuccess() && response.body().success) {
            Log.i(TAG, "success");
            if (networkListener != null) {
                networkListener.onOK(response.body());
            }
            return true;
        } else {
            String mess = response.isSuccess() ? response.body().msg : response.message();
            Log.w(TAG, mess);
            if (networkListener != null) {
                networkListener.onError(mess);
            }
            return false;
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

    // TODO: 15/9/16 how to abstract
    //    protected <T extends OnlySuccess>Callback<T> getCallback() {
    //        return new Callback<T>() {
    //            @Override
    //            public void onResponse(T response) {
    //                myOnResponse(response);
    //            }
    //
    //            @Override
    //            public void onFailure(Throwable t) {
    //                myOnFailure(t);
    //            }
    //        };
    //    }
}
