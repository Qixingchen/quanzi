package com.tizi.quanzi.network;


import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tizi.quanzi.log.Log;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/9/15.
 * Retrofit
 */
public class RetrofitNetwork {
    private final static String TAG = RetrofitNetwork.class.getSimpleName();

    //    private static final String URL = "http://120.25.232.240:8080";
    private static final String URL = "http://192.168.3.16:8080";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .client(getclient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    /**
     * OKHttp log接口
     */
    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            //Log.i(TAG, String.format("Sending request %s on %s%n%s",
            //request.url(), chain.connection(), "request.headers()"));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i(TAG, String.format("Received response for %s in %.1fms%n",
                    response.request().url(), (t2 - t1) / 1e6d));

            return response;
        }
    }

    private static OkHttpClient getclient() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new LoggingInterceptor());
        return client;
    }

}
