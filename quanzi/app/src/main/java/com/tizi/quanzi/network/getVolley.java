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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by qixingchen on 15/7/13.
 */
public class GetVolley {

    private static GetVolley mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private static Response.Listener<String> mOKListener;
    private static Response.ErrorListener mErrorListener;

    private static final String TAG = GetVolley.class.getSimpleName();


    public static GetVolley getmInstance(Context context) {
        if (mInstance == null) {
            synchronized (GetVolley.class) {
                if (mInstance == null) {
                    mInstance = new GetVolley(context.getApplicationContext());
                }
            }
        }
        mErrorListener = makeErrorListener();
        return mInstance;
    }

    public static GetVolley getmInstance(Context context, Response.Listener OKListener, Response.ErrorListener errorListener) {
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

    public static GetVolley getmInstance(Context context, Response.Listener OKListener) {
        if (mInstance == null) {
            synchronized (GetVolley.class) {
                if (mInstance == null) {
                    mInstance = new GetVolley(context);
                }
            }
        }
        mOKListener = OKListener;
        mErrorListener = makeErrorListener();
        return mInstance;
    }

    private GetVolley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

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

    public <T> GetVolley addToRequestQueue(Request<T> req) {
        Log.w(TAG, req.getUrl());
        getRequestQueue().add(req);
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public GetVolley addRequestWithSign(int method, String baseuri, Map<String, String> para) {
        String uri = baseuri + "?";
        para = addSignMap(para);
        String paraUri = getParaUriNoSigned(para);
        paraUri += "&sign=" + getSignString(para.get("ts"), para.get("userid"));
        uri += paraUri;
        StringRequest stringRequest = new StringRequest(method, uri, mOKListener, mErrorListener);
        addToRequestQueue(stringRequest);
        return mInstance;
    }

    private Map<String, String> addSignMap(Map<String, String> para) {
        para.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
        para.put("userid", App.getUserID());
        return para;
    }

    private String getParaUriNoSigned(Map<String, String> para) {
        String paraUri = "";
        for (Map.Entry<String, String> entry : para.entrySet()) {
            paraUri += entry.getKey() + "=" + entry.getValue() + "&";
        }
        paraUri = paraUri.substring(0, paraUri.length() - 1);
        return paraUri;
    }

    private String getSignString(String ts, String userid) {

        String para = "ts=" + ts + "userid=" + userid;
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

    public GetVolley addPostRequestWithSign(int method, String baseuri, final Map<String, String> para) {

        StringRequest stringRequest = new StringRequest(method, baseuri,
                mOKListener, mErrorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map paraAdded = addPostSignPara(para);
                return paraAdded;
            }
        };
        addToRequestQueue(stringRequest);
        return mInstance;
    }

    private Map addPostSignPara(Map<String, String> para) {
        para = addSignMap(para);
        para.put("sign", getSignString(para.get("ts"), App.getUserID()));
        return para;
    }

}
