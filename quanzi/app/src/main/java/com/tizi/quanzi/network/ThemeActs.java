package com.tizi.quanzi.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.tizi.quanzi.R;

import java.util.Map;
import java.util.TreeMap;

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
        Map<String, String> para = new TreeMap<>();

        GetVolley.getmInstance().setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/act/get", para);
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
        Map<String, String> para = new TreeMap<>();
        para.put("actid", actID);
        para.put("grpid", groupID);
        para.put("flag", String.valueOf(flag));

        GetVolley.getmInstance().setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/act/sign", para);
    }
}
