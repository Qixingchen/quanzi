package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.tool.Tool;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by qixingchen on 15/9/11.
 * 主题相关后台
 *
 * @see com.tizi.quanzi.network.RetrofitAPI.Themes
 */
public class ThemeActs extends RetrofitNetworkAbs {

    private RetrofitAPI.Themes themeService = RetrofitNetwork.retrofit.create(RetrofitAPI.Themes.class);


    private ThemeActs() {
    }

    /**
     * @return 新的实例
     */
    public static ThemeActs getNewInstance() {
        return new ThemeActs();
    }

    /**
     * 获取活动
     */
    public void getThemes() {

        final Call<Theme> themeCall = themeService.getThemes();

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

    /**
     * 获取指定活动的热门评论
     *
     * @param themeID 活动的ID
     */
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

    /**
     * 获取自己已经参加活动的群
     *
     * @param themeID 活动ID
     */
    public void getMySignedGroups(String themeID) {
        themeService.querySignedGroup(themeID, Tool.getSignMap())
                .enqueue(new Callback<GroupIDs>() {
                    @Override
                    public void onResponse(Response<GroupIDs> response) {
                        myOnResponse(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        myOnFailure(t);
                    }
                });
    }

    /**
     * (取消)报名参加指定活动
     *
     * @param actID   活动ID
     * @param groupID 群ID
     * @param flag    报名：1 取消报名：0
     */
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

    /**
     * 获取碰撞的圈子
     */
    public void getBoomGroup(String themeID) {
        themeService.getBoomGroup(themeID).enqueue(new Callback<BoomGroup>() {
            @Override
            public void onResponse(Response<BoomGroup> response) {
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
