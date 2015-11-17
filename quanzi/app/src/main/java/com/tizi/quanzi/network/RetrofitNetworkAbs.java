package com.tizi.quanzi.network;

import android.os.Build;

import com.avos.avoscloud.AVObject;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

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

    /**
     * 检查Response是否成功
     *
     * @param response retrofit 的 response
     *
     * @return 是否成功
     */
    protected boolean myOnResponse(retrofit.Response<? extends OnlySuccess> response) {
        try {
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
        } catch (Exception ex) {
            saveTExceptionToLenCloud(ex);
        }
        return false;
    }

    /**
     * OnFailure
     */
    protected void myOnFailure(Throwable t) {
        Log.w(TAG, t.getMessage());
        try {
            if (networkListener != null) {
                if (t.getMessage() == null || t.getMessage().compareTo("") == 0) {
                    networkListener.onError("网络错误");
                } else {
                    networkListener.onError(t.getMessage());
                }
            }
        } catch (Exception ex) {
            saveTExceptionToLenCloud(ex);
        }
    }

    private void saveTExceptionToLenCloud(Exception ex) {
        ex.printStackTrace();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        AVObject logRecord = new AVObject("Android_Log");
        logRecord.put("App_version", BuildConfig.VERSION_NAME);
        logRecord.put("App_version_Code", BuildConfig.VERSION_CODE);
        logRecord.put("StackTrace", sw.toString());
        logRecord.put("Cause", ex.getCause() == null ? "" : ex.getCause().getMessage());
        logRecord.put("DEVICE", Build.DEVICE);
        logRecord.put("MANUFACTURER", Build.MANUFACTURER);
        logRecord.put("MODEL", Build.MODEL);
        logRecord.put("SDK", Build.VERSION.SDK_INT);
        logRecord.put("HARDWARE", Build.HARDWARE);
        logRecord.saveInBackground();
    }


    // TODO: 15/9/17 T 抽象
    public interface NetworkListener<T extends Object> {
        // TODO: 15/8/20 why
        void onOK(T ts);

        void onError(String Message);
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
