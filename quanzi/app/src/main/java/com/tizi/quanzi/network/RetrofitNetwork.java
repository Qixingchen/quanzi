package com.tizi.quanzi.network;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/9/15.
 * Retrofit
 */
public class RetrofitNetwork {
    private static final String URL = "http://120.25.232.240:8080";
    //    private static final String URL = "http://192.168.3.16:8080";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
