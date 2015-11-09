package com.tizi.quanzi.network;


import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.log.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by qixingchen on 15/9/15.
 * Retrofit
 */
public class RetrofitNetwork {
    private final static String TAG = RetrofitNetwork.class.getSimpleName();

    private static final String URL = "http://120.25.232.240:8080/";
    //    private static final String URL = "http://192.168.3.2:8080/";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .client(getclient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private static OkHttpClient getclient() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new LoggingInterceptor());
        client.networkInterceptors().add(new SignInterceptor());
        return client;
    }

    /**
     * 从 URL 获取参数
     *
     * @param url URL
     *
     * @return 参数MAP
     */
    private static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new TreeMap<>();
        String query = url.getQuery();
        if (query == null) {
            return query_pairs;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    /**
     * 获取签名串
     * Token 将从App.getUserToken() 加载
     *
     * @return sign的值
     */
    private static String getSignString() {

        String ts = String.valueOf((System.currentTimeMillis() + AppStaticValue.timeAddtion) / 1000L);
        String userid = AppStaticValue.getUserID();

        String para = "ts=" + ts + "&uid=" + userid;
        para += AppStaticValue.getUserToken();

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

        return "ts=" + ts + "&uid=" + userid + "&sign=" + sign;
    }

    /**
     * OKHttp log接口
     */
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            //Log.i(TAG, String.format("Sending request %s on %s%n%s",
            //request.url(), chain.connection(), "request.headers()"));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            // TODO: 15/9/25 disable signed url
            Log.i(TAG, String.format("Received response for %s in %.1fms%n",
                    response.request().url(), (t2 - t1) / 1e6d));
            //Log.i(TAG,response.body().string());

            return response;
        }
    }

    /**
     * 签名接口
     */
    private static class SignInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            java.net.URL requestURL = request.url();
            if (requestURL.getHost().compareTo(new URL(URL).getHost()) != 0) {
                return chain.proceed(request);
            }

            Map<String, String> queryMap = splitQuery(requestURL);
            if (queryMap.containsKey("sign")) {
                return chain.proceed(request);
            } else {
                Request.Builder signedRequestBuilder = request.newBuilder();
                if (queryMap.size() == 0) {
                    signedRequestBuilder.url(requestURL + "?" + getSignString());
                } else {
                    signedRequestBuilder.url(requestURL + "&" + getSignString());
                }
                request = signedRequestBuilder.build();
                return chain.proceed(request);
            }
        }
    }

}
