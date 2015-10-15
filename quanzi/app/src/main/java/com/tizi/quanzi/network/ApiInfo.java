package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.ApiInfoGson;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/9/18.
 * 后台API信息
 */
public class ApiInfo extends RetrofitNetworkAbs {
    private RetrofitAPI.ApiInfo apiInfoSer = RetrofitNetwork.retrofit.create(RetrofitAPI.ApiInfo.class);

    public static ApiInfo getNewInstance() {
        return new ApiInfo();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ApiInfo setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }

    public void getAPiinfo() {
        apiInfoSer.getApiVer().enqueue(new Callback<ApiInfoGson>() {
            @Override
            public void onResponse(Response<ApiInfoGson> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }
}
