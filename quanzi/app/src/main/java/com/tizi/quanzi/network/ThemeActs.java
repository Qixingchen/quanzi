package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.gson.GroupSignUPThemeAns;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.tool.Tool;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            public void onResponse(Call<Theme> call, Response<Theme> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<Theme> call, Throwable t) {
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

        final Call<HotDyns> hotDynsCall = themeService.getHotDyns(themeID);

        hotDynsCall.enqueue(new Callback<HotDyns>() {
            @Override
            public void onResponse(Call<HotDyns> call, Response<HotDyns> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<HotDyns> call, Throwable t) {
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
        themeService.querySignedGroup(themeID)
                .enqueue(new Callback<GroupIDs>() {
                    @Override
                    public void onResponse(Call<GroupIDs> call, Response<GroupIDs> response) {
                        myOnResponse(response);
                    }

                    @Override
                    public void onFailure(Call<GroupIDs> call, Throwable t) {
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
        themeService.signUp(actID, groupID, flag).enqueue(new Callback<GroupSignUPThemeAns>() {
            @Override
            public void onResponse(Call<GroupSignUPThemeAns> call, Response<GroupSignUPThemeAns> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<GroupSignUPThemeAns> call, Throwable t) {
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
        final Call<OnlySuccess> themeCall = themeService.feedBack(actID, Tool.getUTF_8String(feedback));

        themeCall.enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Call<OnlySuccess> call, Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<OnlySuccess> call, Throwable t) {
                myOnFailure(t);
            }
        });

    }

    /**
     * 获取碰撞的圈子
     *
     * @param themeID 主题ID
     */
    public void getBoomGroup(String themeID) {
        themeService.getBoomGroup(themeID).enqueue(new Callback<BoomGroup>() {
            @Override
            public void onResponse(Call<BoomGroup> call, Response<BoomGroup> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<BoomGroup> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 获取碰撞的圈子
     *
     * @param themeID 主题ID
     * @param groupID 圈子ID
     */
    public void getBoomGroup(String themeID, String groupID) {
        themeService.getBoomGroup(themeID, groupID).enqueue(new Callback<BoomGroup>() {
            @Override
            public void onResponse(Call<BoomGroup> call, Response<BoomGroup> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<BoomGroup> call, Throwable t) {
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
