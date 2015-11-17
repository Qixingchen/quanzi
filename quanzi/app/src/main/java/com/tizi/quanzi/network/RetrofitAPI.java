package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.AddComment;
import com.tizi.quanzi.gson.AddZan;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.ApiInfoGson;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.gson.GroupInviteAns;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.IsZan;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.gson.Theme;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by qixingchen on 15/9/15.
 * Retrofit 网络接口
 */
public interface RetrofitAPI {


    interface Themes {
        @POST("act/get")
        Call<Theme> getThemes();

        @POST("act/sign")
        Call<OnlySuccess> signUp(
                @Query("actid") String actID,
                @Query("grpid") String groupID,
                @Query("flag") int flag);

        @POST("act/feedback")
        Call<OnlySuccess> feedBack(
                @Query("actid") String actID,
                @Query(value = "feedback", encoded = true) String feedBack);

        @POST("grpdyn/getHot")
        Call<HotDyns> getHotDyns(
                @Query("actid") String themeID);

        @POST("grpdyn/getTop")
        Call<HotDyns> getTopDyns(
                @Query("actid") String themeID);

        @POST("act/mysign")
        Call<GroupIDs> querySignedGroup(
                @Query("actid") String ThemeID);

        @POST("group/collide")
        Call<BoomGroup> getBoomGroup(
                @Query("actid") String ThemeID
        );

        @POST("group/collide")
        Call<BoomGroup> getBoomGroup(
                @Query("actid") String ThemeID,
                @Query("groupid") String groupID
        );
    }

    interface Group {
        /*全有*/
        @POST("group/createF")
        Call<com.tizi.quanzi.gson.Group> addGroup(
                @Query("userid") String userID,
                @Query("groupname") String groupName,
                @Query(value = "grouptags", encoded = true) String groupTags,
                @Query("icon") String icon,
                @Query(value = "notice", encoded = true) String notice,
                @Query("convid") String convID);

        /*没有TAG*/
        @POST("group/createF")
        Call<com.tizi.quanzi.gson.Group> addGroup(
                @Query("userid") String userID,
                @Query("groupname") String groupName,
                @Query("icon") String icon,
                @Query(value = "notice", encoded = true) String notice,
                @Query("convid") String convID);

        @POST("group/findGroupAllInfoF")
        Call<GroupAllInfo> queryGroup(
                @Query("groupid") String groupID);

        @POST("group/findGroupAllInfoF")
        public Observable<GroupAllInfo> queryGroupRX(
                @Query("groupid") String groupID);

        @POST("group/findTagF")
        Call<AllTags> queryAllAvailableTag();

        @POST("group/findTagF")
        Call<AllTags> queryAllAvailableUserTag(
                @Query("type") String type //user:1 group:0/null
        );

        @POST("group/updateFieldF")
        Call<OnlySuccess> changeGroupInfo(
                @Query("groupid") String groupID,
                @Query("field") String field,
                @Query(value = "value", encoded = true) String value);

        @POST("group/findGroupTags")
        Call<AllTags> queryGroupTAG(
                @Query("groupid") String groupID);


    }

    interface GroupMember {
        @POST("group/acceptGroupInvite")
        Call<GroupInviteAns> acceptGroupInvite(
                @Query("groupid") String groupID,
                @Query("userid") String userid);

        @POST("group/exitGroupF")
        Call<OnlySuccess> exitOrDeleteMember(
                @Query("groupid") String groupID,
                @Query("userid") String exitUserID);

        @POST("group/dropGroupF")
        Call<OnlySuccess> dropGroup(
                @Query("groupid") String groupID);
    }

    interface Follow {

        @POST("group/followGroupF")
        Call<OnlySuccess> followGroup(
                @Query("groupid") String groupID);

        @POST("group/cancelFollowF")
        Call<OnlySuccess> cancelFollowGroup(
                @Query("groupid") String groupID);

    }

    interface Dyns {
        @POST("grpdyn/addF")
        Call<OnlySuccess> addDyn(
                @Query("actid") String themeID,
                @Query("grpid") String groupID,
                @Query(value = "content", encoded = true) String content,
                @Query(value = "pics", encoded = true) String pics);

        @POST("grpdyn/addF")
        Call<OnlySuccess> addDyn(
                @Query("actid") String themeID,
                @Query("grpid") String groupID,
                @Query(value = "content", encoded = true) String content);

        @POST("grpdyn/delF")
        Call<OnlySuccess> delDyn(
                @Query("dynid") String dynID);

        @POST("grpdyn/findF")
        Call<com.tizi.quanzi.gson.Dyns> findDyns(
                @Query("actid") String actID,
                @Query("senderid") String groupID,
                @Query("start") int start,
                @Query("limit") int limit);

        @POST("grpdyn/findF")
        Call<com.tizi.quanzi.gson.Dyns> findGroupDyns(
                @Query("senderid") String groupID,
                @Query("start") int start,
                @Query("limit") int limit);

        @POST("grpdyn/findF")
        Call<com.tizi.quanzi.gson.Dyns> findThemeDyns(
                @Query("actid") String actID,
                @Query("start") int start,
                @Query("limit") int limit);

        @POST("grpdyn/findF")
        Call<com.tizi.quanzi.gson.Dyns> findDynByID(
                @Query("dynid") String dynID);

        @POST("grpdyn/zanF")
        Call<AddZan> zan(
                @Query("dynid") String dynID,
                @Query("zan") int isZan);

        @POST("grpdyn/addComment")
        Call<AddComment> addComent(
                @Query("dynid") String dynID,
                @Query(value = "comment", encoded = true) String comment);

        @POST("grpdyn/addComment")
        Call<AddComment> addComent(
                @Query("dynid") String dynID,
                @Query(value = "comment", encoded = true) String comment,
                @Query("replyid") String replyID,
                @Query("atuserid") String AtUserID);

        @POST("grpdyn/delComment")
        Call<OnlySuccess> deleteComment(
                @Query("cid") String commitID);

        @POST("grpdyn/findComment")
        Call<Comments> findComment(
                @Query("dynid") String dynid,
                @Query("start") int start,
                @Query("limit") int limit);

        @POST("grpdyn/isZan")
        Call<IsZan> isZan(
                @Query("dynid") String dynid
        );

    }

    interface FindUser {
        @POST("user/findUserF")
        Call<OtherUserInfo> getUserByAccount(
                @Query("account") String account);

        @POST("user/findUserF")
        Call<OtherUserInfo> getUserByID(
                @Query("userid") String userID);

        @POST("user/findByContact")
        Call<ContantUsers> findContactUser(
                @Query(value = "mobiles", encoded = true) String mpbiles);
    }

    interface ApiInfo {
        @POST("apiinfo/info")
        Call<ApiInfoGson> getApiVer();
        // TODO: 15/9/16 on login and on time change
        //http://stackoverflow.com/questions/5481386/date-and-time-change-listener-in-android
    }

    interface UserAccount {
        @POST("applogin/regF")
        Call<Login> register(
                @Query("account") String account,
                @Query("username") String userName,
                @Query("password") String password,
                @Query("sex") String sex,
                @Query("icon") String icon,
                @Query("clienttype") String clientType,
                @Query("clientversion") String clientVersion,
                @Query("clientmac") String clientMAC);

        @POST("applogin/updatePassF")
        Call<OnlySuccess> changePassword(
                @Query("account") String account,
                @Query("password") String password);

        @POST("applogin/loginF")
        Call<Login> login(
                @Query("account") String account,
                @Query("password") String password);

        @POST("user/updateFieldF")
        Call<OnlySuccess> changeUserInfo(
                @Query("field") String field,
                @Query(value = "value", encoded = true) String value,
                @Query("userid") String userID);


    }

    interface BaiduLocation {
        @GET("/location/ip")
        Call<com.tizi.quanzi.gson.BaiduLocation> getLocation(
                @Query("ak") String ak,
                @Query("coor") String coor
        );
    }

}


















