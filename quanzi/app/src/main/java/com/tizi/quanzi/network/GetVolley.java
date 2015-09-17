package com.tizi.quanzi.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.tizi.quanzi.app.App;

/**
 * Created by qixingchen on 15/7/13.
 * volley 网络交互
 */
public class GetVolley {

    private static GetVolley mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private static final String TAG = GetVolley.class.getSimpleName();

    /**
     * 获得实例
     *
     * @return GetVolley实例
     */
    public static GetVolley getmInstance() {
        if (mInstance == null) {
            synchronized (GetVolley.class) {
                if (mInstance == null) {
                    mInstance = new GetVolley(App.getApplication());
                }
            }
        }
        return mInstance;
    }


    private GetVolley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(2000);

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

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
