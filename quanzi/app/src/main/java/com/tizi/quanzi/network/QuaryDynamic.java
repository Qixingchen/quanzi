package com.tizi.quanzi.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.log.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qixingchen on 15/8/18.
 * 动态查询
 */
public class QuaryDynamic {

    private static QuaryDynamic mInstance;
    private static final String TAG = QuaryDynamic.class.getSimpleName();
    private Gson gson;
    private Context mContext;

    private QuaryDynamicListener quaryDynamicListener;

    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    public QuaryDynamic setQuaryDynamicListener(QuaryDynamicListener quaryDynamicListener) {
        this.quaryDynamicListener = quaryDynamicListener;
        return mInstance;
    }

    public static QuaryDynamic getInstance() {
        if (mInstance == null) {
            synchronized (QuaryDynamic.class) {
                if (mInstance == null) {
                    mInstance = new QuaryDynamic();
                }
            }
        }
        return mInstance;
    }

    private QuaryDynamic() {
        this.mContext = App.getApplication();
        gson = new Gson();
        mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Dyns dyns = gson.fromJson(response, Dyns.class);
                if (dyns.success) {
                    if (quaryDynamicListener != null) {
                        quaryDynamicListener.onOK(dyns);
                    }
                } else {
                    Log.e(TAG, dyns.msg);
                    Toast.makeText(mContext, dyns.msg, Toast.LENGTH_LONG).show();
                    quaryDynamicListener.onError();
                }
            }
        };
        mErrorListener = new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                if (quaryDynamicListener != null) {
                    quaryDynamicListener.onError();
                }
            }
        }

        ;
    }

    /**
     * 查询圈子类所有人的动态
     */
    public QuaryDynamic getQuanZiDynamic() {

        Map<String, String> quaryDynmicPara = new TreeMap<>();
        //quaryDynmicPara.put("userid", App.getUserID());

        GetVolley.getmInstance(mContext).setOKListener(mOKListener).
                setErrorListener(mErrorListener)
                .addRequestWithSign(Request.Method.GET,
                        mContext.getString(R.string.testbaseuri) + "/grpdyn/findF", quaryDynmicPara);
        return mInstance;
    }

    public interface QuaryDynamicListener {
        /**
         * 成功回调
         *
         * @param dyns 动态信息
         */
        void onOK(Dyns dyns);

        void onError();
    }

}
