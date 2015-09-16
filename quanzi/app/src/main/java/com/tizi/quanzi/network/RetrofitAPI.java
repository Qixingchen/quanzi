package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.OtherUserInfo;
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

        @POST("/act/mysign")
        Call<GroupIDs> querySignedGroup(
                @Query("actid") String ThemeID,
                @QueryMap Map<String, String> signMap);
    }

    interface Group {
        @POST("/group/createF")
        Call<GroupAllInfo> addGroup(
                @Query("groupname") String groupName,
                @Query(value = "grouptags", encoded = true) String groupTags,
                @Query("icon") String icon,
                @Query("notice") String notice,
                @Query("userid") String userID,// TODO: 15/9/16 delete
                @Query("convid") String convID,
                @QueryMap Map<String, String> signMap);

        @POST("/group/findGroupAllInfoF")
        Call<GroupAllInfo> queryGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

        @POST("/group/findTagF")
        Call<GroupAllInfo.TagListEntity> queryAllAvailableTag(
                @QueryMap Map<String, String> signMap);

        @POST("/group/updateFieldF")
        Call<OnlySuccess> changeGroupInfo(
                @Query("groupid") String groupID,
                @Query("field") String field,
                @Query("value") String value,
                @QueryMap Map<String, String> signMap);

        @POST("/group/findGroupTags")
        Call<GroupAllInfo.TagListEntity> queryGroupTAG(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);


    }

    interface GroupMember {
        @POST("/group/acceptGroupInvite")
        Call<GroupAllInfo> acceptGroupInvite(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

        @POST("/group/exitGroupF")
        Call<OnlySuccess> exitGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

        @POST("/group/dropGroupF")
        Call<OnlySuccess> dropGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);
    }

    interface Follow {

        @POST("/group/followGroupF")
        Call<OnlySuccess> followGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

        @POST("/group/cancelFollowF")
        Call<OnlySuccess> cancelFollowGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

    }

    interface dyns {
        @POST("/grpdyn/addF")
        Call<OnlySuccess> addDyn(
                @Query("actid") String themeID,
                @Query("grpid") String groupID,
                @Query("pics") String pics,
                @QueryMap Map<String, String> signMap);

        @POST("/grpdyn/delF")
        Call<OnlySuccess> delDyn(
                @Query("dynid") String dynID,
                @QueryMap Map<String, String> signMap);

        @POST("/grpdyn/findF")
        Call<Dyns> findDyns(
                @Query("actid") String actID,
                @Query("grpid") String groupID,
                @Query("start") int start,
                @Query("limit") int limit,
                @QueryMap Map<String, String> signMap);

        @POST("/grpdyn/zanF")
            // TODO: 15/9/16 get zan -> onlySuccess
        Call<OnlySuccess> zan(
                @Query("dynid") String dynID,
                @Query("zan") int isZan,
                @QueryMap Map<String, String> signMap);

        @POST("/grpdyn/addComment")
        Call<OnlySuccess> addCommit(
                @Query("dynid") String dynID,
                @Query("commit") String commit,
                @QueryMap Map<String, String> signMap);

        @POST("/grpdyn/delComment")
        Call<OnlySuccess> deleteCommit(
                @Query("cid") String commitID,
                @QueryMap Map<String, String> signMap);

    }

    interface FindUser {
        @POST("/user/findUserF")
        Call<OtherUserInfo> getUserByAccount(
                @Query("account") String account,
                @QueryMap Map<String, String> signMap);

        @POST("/user/findUserF")
        Call<OtherUserInfo> getUserByID(
                @Query("account") String userID,
                @QueryMap Map<String, String> signMap);

        @POST("/user/findByContact")
        Call<ContantUsers> findContactUser(
                @Query("mobiles") String mpbiles,
                @QueryMap Map<String, String> signMap);
    }

    interface ApiInfo {
        @POST("appinfo/info")
        Call<ApiInfo> getApiVer();
        // TODO: 15/9/16 on login and on time change
        //http://stackoverflow.com/questions/5481386/date-and-time-change-listener-in-android
    }

    interface UserAccount {
        @POST("/applogin/regF")
        Call<Login> register(
                @Query("account") String account,
                @Query("username") String userName,
                @Query("password") String password,
                @Query("sex") String sex,
                @Query("icon") String icon,
                @Query("clienttype") String clientType,
                @Query("clientversion") String clientVersion,
                @Query("clientmac") String clientMAC);

        @POST("/applogin/updatePassF")
        Call<OnlySuccess> changePassword(
                @Query("account") String account,
                @Query("password") String password);

        @POST("/applogin/loginF")
        Call<Login> login(
                @Query("account") String account,
                @Query("password") String password,
                @QueryMap Map<String, String> signMap);

        @POST("/user/updateFieldF")
        Call<OnlySuccess> changeUserInfo(
                @Query("field") String field,
                @Query("value") String value,
                @QueryMap Map<String, String> signMap);


    }

}


















