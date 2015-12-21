package com.tizi.quanzi.network;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.tizi.quanzi.gson.BaiduLocation;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.DevSettings;

import java.io.IOException;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/11/14.
 * 从 百度 地图 API 获取 地址
 */
public class GetLocationFromBaidu {

    private final static String TAG = GetLocationFromBaidu.class.getSimpleName();

    private static final String URL = "http://api.map.baidu.com";
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .client(getclient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private onResult onResult;

    public static GetLocationFromBaidu getNewInstance(onResult onResult) {
        GetLocationFromBaidu getLocationFromBaidu = new GetLocationFromBaidu();
        getLocationFromBaidu.onResult = onResult;
        return getLocationFromBaidu;
    }

    private static OkHttpClient getclient() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new LoggingInterceptor());
        return client;
    }

    public void getLocation() {
        if (DevSettings.disallowBaiduLocation()) {
            onResult.error("百度定位已禁用");
            return;
        }
        retrofit.create(RetrofitAPI.BaiduLocation.class).getLocation("CPuqLrXjjpwuSoZFBl8qHFPb", "bd09ll")
                .enqueue(new Callback<BaiduLocation>() {
                    @Override
                    public void onResponse(Response<BaiduLocation> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body().status == 0) {
                            BaiduLocation.ContentEntity content = response.body().content;
                            onResult.ok(content.addressDetail.province + content.addressDetail.city,
                                    content.point.x, content.point.y);
                        } else {
                            onResult.error(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        onResult.error(t.getMessage());
                    }
                });
    }

    public interface onResult {
        void ok(String area, Double Longitude, Double Latitude);

        void error(String errorMessage);
    }

    /**
     * OKHttp log接口
     */
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            //Log.i(TAG, String.format("Sending request %s on %s%n%s",
            //request.url(), chain.connection(), "request.headers()"));

            com.squareup.okhttp.Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            // TODO: 15/9/25 disable signed url
            Log.i(TAG, String.format("Received response for %s in %.1fms%n",
                    response.request().url(), (t2 - t1) / 1e6d));
            //Log.i(TAG,response.body().string());

            return response;
        }
    }
}
