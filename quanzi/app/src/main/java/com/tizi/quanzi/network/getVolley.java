package com.tizi.quanzi.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.log.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by qixingchen on 15/7/13.
 * volley 网络交互
 */
public class GetVolley {

    private static GetVolley mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    private static final String TAG = GetVolley.class.getSimpleName();

    /**
     * 获得实例
     *
     * @param context 上下文
     *
     * @return GetVolley实例
     */
    public static GetVolley getmInstance(Context context) {
        getmInstance(context, null, makeErrorListener());
        return mInstance;
    }

    /**
     * 获得实例
     *
     * @param context    上下文
     * @param OKListener 成功监听器
     *
     * @return GetVolley实例
     */
    public static GetVolley getmInstance(Context context, Response.Listener<String> OKListener) {
        getmInstance(context, OKListener, makeErrorListener());
        return mInstance;
    }

    /**
     * 获得实例
     *
     * @param context       上下文
     * @param OKListener    成功监听器
     * @param errorListener 失败监听器
     *
     * @return GetVolley实例
     */
    public static GetVolley getmInstance(Context context, Response.Listener<String> OKListener,
                                         Response.ErrorListener errorListener) {
        if (mInstance == null) {
            synchronized (GetVolley.class) {
                if (mInstance == null) {
                    mInstance = new GetVolley(context.getApplicationContext());
                }
            }
        }
        mOKListener = OKListener;
        mErrorListener = errorListener;
        return mInstance;
    }


    private GetVolley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(2000);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * 将请求加入队列
     *
     * @param req 请求
     */
    public <T> GetVolley addToRequestQueue(Request<T> req) {
        Log.w(TAG, req.getUrl());
        getRequestQueue().add(req);
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 将基底uri和参数串合成为带签名的访问链接
     * 并加入请求
     *
     * @param method  请求方式（GET／POST等）
     * @param baseuri 基底Uri
     * @param para    参数串
     */
    public GetVolley addRequestWithSign(int method, String baseuri, Map<String, String> para) {
        String uri = baseuri + "?";
        para = addSignMap(para);
        String paraUri = convertToUTF8(getParaUriFromMap(para));
        uri += paraUri;
        StringRequest stringRequest = new StringRequest(method, uri, mOKListener, mErrorListener);
        addToRequestQueue(stringRequest);
        return mInstance;
    }

    /**
     * 将签名信息加入Map
     *
     * @param para 原始参数串
     *
     * @return 带预签名信息的参数串
     */
    private Map<String, String> addSignMap(Map<String, String> para) {
        para.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
        para.put("uid", App.getUserID());
        para.put("sign", getSignString(para.get("ts"), App.getUserID()));
        return para;
    }

    /**
     * 将请求串整理成链接（不带基底uri）
     * todo 转义
     *
     * @param para 请求串
     *
     * @return 请求链接
     */
    private String getParaUriFromMap(Map<String, String> para) {
        String paraUri = "";
        for (Map.Entry<String, String> entry : para.entrySet()) {
            paraUri += entry.getKey() + "=" + entry.getValue() + "&";
        }
        paraUri = paraUri.substring(0, paraUri.length() - 1);
        return paraUri;
    }

    /**
     * 获取签名串
     * Token 将从App.getUserToken() 加载
     *
     * @param ts     签名串的ts
     * @param userid 签名串的Userid
     *
     * @return sign的值
     */
    private String getSignString(String ts, String userid) {

        String para = "ts=" + ts + "&uid=" + userid;
        para += App.getUserToken();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null;
        byte[] Md5 = md.digest(para.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : Md5) {
            int bt = b & 0xff;
            if (bt < 16) {
                stringBuffer.append(0);
            }
            stringBuffer.append(Integer.toHexString(bt));
        }
        String sign = stringBuffer.toString();
        return sign;
    }

    public GetVolley setOKListener(Response.Listener<String> OKListener) {
        mOKListener = OKListener;
        return mInstance;
    }

    public GetVolley setErrorListener(Response.ErrorListener ErrorListener) {
        mErrorListener = ErrorListener;
        return mInstance;
    }

    private static Response.ErrorListener makeErrorListener() {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        };
        return errorListener;
    }

    /**
     * 将基底uri和参数串合成为带签名的访问串（Post）
     * 并加入请求
     * todo 整合
     *
     * @param method  请求方式（GET／POST等）
     * @param baseuri 基底Uri
     * @param para    参数串
     */
    public GetVolley addPostRequestWithSign(int method, String baseuri, final Map<String, String> para) {

        StringRequest stringRequest = new StringRequest(method, baseuri,
                mOKListener, mErrorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map paraAdded = addSignMap(para);
                return paraAdded;
            }
        };
        addToRequestQueue(stringRequest);
        return mInstance;
    }

    private String convertToUTF8(String s) {
        Log.w(TAG, s);

        return new String(s.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

}
