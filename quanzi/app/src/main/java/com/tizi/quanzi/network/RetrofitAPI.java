package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.Theme;

import java.util.Map;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by qixingchen on 15/9/15.
 * Retrofit 网络接口
 */
public interface RetrofitAPI {


    interface Themes {
        @POST("/act/get")
        Call<Theme> getThemes(
                @QueryMap Map<String, String> signMap);

        @POST("/act/sign")
        Call<OnlySuccess> signUp(
                @Query("actid") String actID,
                @Query("grpid") String groupID,
                @Query("flag") int flag,
                @QueryMap Map<String, String> signMap);

        @POST("/act/feedback")
        Call<OnlySuccess> feedBack(
                @Query("actid") String actID,
                @Query("feedback") String feedBack,
                @QueryMap Map<String, String> signMap);

        @POST("/grpdyn/getHot")
        Call<HotDyns> getHotDyns(
                @Query("actid") String themeID,
                @QueryMap Map<String, String> signMap);


    }

}
