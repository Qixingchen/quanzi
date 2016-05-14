package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.ApiInfoGson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            public void onResponse(Call<ApiInfoGson> call, Response<ApiInfoGson> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<ApiInfoGson> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }
}
