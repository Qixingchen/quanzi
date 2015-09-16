package com.tizi.quanzi.network;

import android.content.Context;

import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.tool.Tool;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by qixingchen on 15/9/11.
 *
 */
public class ThemeActs extends RetrofitNetworkAbs {

    private RetrofitAPI.Themes themeService = RetrofitNetwork.retrofit.create(RetrofitAPI.Themes.class);


    private ThemeActs() {
    }

    /**
     * @param context 上下文
     *
     * @return 新的实例
     */
    public static ThemeActs getNewInstance(Context context) {
        return new ThemeActs();
    }

    public void getThemes() {

        final Call<Theme> themeCall = themeService.getThemes(Tool.getSignMap());

        themeCall.enqueue(new Callback<Theme>() {
            @Override
            public void onResponse(retrofit.Response<Theme> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });

    }

    public void getHotDyns(String themeID) {

        final Call<HotDyns> hotDynsCall = themeService.getHotDyns(themeID, Tool.getSignMap());

        hotDynsCall.enqueue(new Callback<HotDyns>() {
            @Override
            public void onResponse(Response<HotDyns> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    public void signUP(String actID, String groupID, int flag) {

        final Call<OnlySuccess> themeCall = themeService.signUp(actID, groupID, flag, Tool.getSignMap());

        themeCall.enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(retrofit.Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 活动反馈
     *
     * @param actID    活动ID
     * @param feedback 反馈
     */
    public void feedBack(String actID, String feedback) {
        final Call<OnlySuccess> themeCall = themeService.feedBack(actID, feedback, Tool.getSignMap());

        themeCall.enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(retrofit.Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });

    }

    @SuppressWarnings("unchecked")
    @Override
    public ThemeActs setNetworkListener(NetworkListener networkListener) {
        return super.setNetworkListener(networkListener, this);
    }
}
