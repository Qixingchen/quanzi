package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.ApiInfoGson;
import com.tizi.quanzi.log.Log;

import retrofit.Callback;
import retrofit.Response;

/**
 * Created by qixingchen on 15/9/18.
 * 后台API信息
 */
public class ApiInfo extends RetrofitNetworkAbs {
    @SuppressWarnings("unchecked")
    @Override
    public ApiInfo setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }

    public static ApiInfo getNewInstance() {
        return new ApiInfo();
    }

    private RetrofitAPI.ApiInfo apiInfoSer = RetrofitNetwork.retrofit.create(RetrofitAPI.ApiInfo.class);

    public void getAPiinfo() {
        apiInfoSer.getApiVer().enqueue(new Callback<ApiInfoGson>() {
            @Override
            public void onResponse(Response<ApiInfoGson> response) {
                if (response.isSuccess()) {
                    Log.i(TAG, "success");
                    if (networkListener != null) {
                        networkListener.onOK(response.body());
                    }
                } else {
                    String mess = response.message();
                    Log.w(TAG, mess);
                    if (networkListener != null) {
                        networkListener.onError(mess);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }
}
