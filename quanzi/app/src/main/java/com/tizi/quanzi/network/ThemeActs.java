package com.tizi.quanzi.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.Tool;

import java.util.Map;
import java.util.TreeMap;

import retrofit.Call;
import retrofit.Callback;

/**
 * Created by qixingchen on 15/9/11.
 */
public class ThemeActs extends NetWorkAbs {
    private static ThemeActs mInstance;

    private ThemeActs() {
        gson = new Gson();
        mOKListener = makeOkListener();
        mErrorListener = makeErrorListener();
    }

    public static ThemeActs getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ThemeActs.class) {
                if (mInstance == null) {
                    mInstance = new ThemeActs();
                }
            }
        }
        // TODO: 15/9/11 why is null
        if (context != null) {
            mContext = context;
        }

        return mInstance;
    }

    public ThemeActs setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
        return mInstance;
    }

    @Override
    protected Response.Listener<String> makeOkListener() {
        Response.Listener<String> OKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                networkListener.onOK(response);
            }
        };
        return OKListener;
    }

    public void getThemes() {

        RetrofitAPI.Themes themeService = RetrofitNetwork.retrofit.create(RetrofitAPI.Themes.class);
        final Call<Theme> themeCall = themeService.getThemes(Tool.getSignMap());

        themeCall.enqueue(new Callback<Theme>() {
            @Override
            public void onResponse(retrofit.Response<Theme> response) {
                checkIsSuccess(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getMessage());
                if (networkListener != null) {
                    networkListener.onError(t.getMessage());
                }
            }
        });

    }

    public void getHotDyns(String themeID) {

        Map<String, String> para = new TreeMap<>();
        para.put("actid", themeID);

        GetVolley.getmInstance().setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/grpdyn/getHot", para);
    }

    public void signUP(String actID, String groupID, int flag) {
        RetrofitAPI.Themes themeService = RetrofitNetwork.retrofit.create(RetrofitAPI.Themes.class);
        final Call<OnlySuccess> themeCall = themeService.signUp(actID, groupID, flag, Tool.getSignMap());

        themeCall.enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(retrofit.Response<OnlySuccess> response) {
                checkIsSuccess(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getMessage());
                if (networkListener != null) {
                    networkListener.onError(t.getMessage());
                }
            }
        });
    }


    private void checkIsSuccess(retrofit.Response<? extends OnlySuccess> response) {
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
}
