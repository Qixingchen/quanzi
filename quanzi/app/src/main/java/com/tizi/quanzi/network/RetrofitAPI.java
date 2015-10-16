package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.AddComment;
import com.tizi.quanzi.gson.ApiInfoGson;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.gson.GroupInviteAns;
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
        @POST("act/get")
        Call<Theme> getThemes();

        @POST("act/sign")
        Call<OnlySuccess> signUp(
                @Query("actid") String actID,
                @Query("grpid") String groupID,
                @Query("flag") int flag,
                @QueryMap Map<String, String> signMap);

        @POST("act/feedback")
        Call<OnlySuccess> feedBack(
                @Query("actid") String actID,
                @Query("feedback") String feedBack,
                @QueryMap Map<String, String> signMap);

        @POST("grpdyn/getHot")
        Call<HotDyns> getHotDyns(
                @Query("actid") String themeID,
                @QueryMap Map<String, String> signMap);

        @POST("act/mysign")
        Call<GroupIDs> querySignedGroup(
                @Query("actid") String ThemeID,
                @QueryMap Map<String, String> signMap);

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
        @POST("group/createF")
        Call<com.tizi.quanzi.gson.Group> addGroup(
                @Query("groupname") String groupName,
                @Query(value = "grouptags", encoded = true) String groupTags,
                @Query("icon") String icon,
                @Query("notice") String notice,
                @Query("userid") String userID,// TODO: 15/9/16 delete
                @Query("convid") String convID,
                @QueryMap Map<String, String> signMap);

        @POST("group/findGroupAllInfoF")
        Call<GroupAllInfo> queryGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

        @POST("group/findTagF")
        Call<GroupAllInfo.TagListEntity> queryAllAvailableTag(
                @QueryMap Map<String, String> signMap);

        @POST("group/updateFieldF")
        Call<OnlySuccess> changeGroupInfo(
                @Query("groupid") String groupID,
                @Query("field") String field,
                @Query("value") String value,
                @QueryMap Map<String, String> signMap);

        @POST("group/findGroupTags")
        Call<GroupAllInfo.TagListEntity> queryGroupTAG(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);


    }

    interface GroupMember {
        @POST("group/acceptGroupInvite")
        Call<GroupInviteAns> acceptGroupInvite(
                @Query("groupid") String groupID,
                @Query("userid") String userid);

        @POST("group/exitGroupF")
        Call<OnlySuccess> exitOrDeleteMember(
                @Query("groupid") String groupID,
                @Query("userid") String exitUserID,
                @QueryMap Map<String, String> signMap);

        @POST("group/dropGroupF")
        Call<OnlySuccess> dropGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);
    }

    interface Follow {

        @POST("group/followGroupF")
        Call<OnlySuccess> followGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

        @POST("group/cancelFollowF")
        Call<OnlySuccess> cancelFollowGroup(
                @Query("groupid") String groupID,
                @QueryMap Map<String, String> signMap);

    }

    interface Dyns {
        @POST("grpdyn/addF")
        Call<OnlySuccess> addDyn(
                @Query("actid") String themeID,
                @Query("grpid") String groupID,
                @Query("pics") String pics);

        @POST("grpdyn/delF")
        Call<OnlySuccess> delDyn(
                @Query("dynid") String dynID);

        @POST("grpdyn/findF")
        Call<com.tizi.quanzi.gson.Dyns> findDyns(
                @Query("actid") String actID,
                @Query("grpid") String groupID,
                @Query("start") int start,
                @Query("limit") int limit);

        @POST("grpdyn/zanF")
            // TODO: 15/9/16 get zan -> onlySuccess
        Call<OnlySuccess> zan(
                @Query("dynid") String dynID,
                @Query("zan") int isZan);

        @POST("grpdyn/addComment")
        Call<OnlySuccess> addComent(
                @Query("dynid") String dynID,
                @Query("comment") String comment);

        @POST("grpdyn/addComment")
        Call<AddComment> addComent(
                @Query("dynid") String dynID,
                @Query("comment") String comment,
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

    }

    interface FindUser {
        @POST("user/findUserF")
        Call<OtherUserInfo> getUserByAccount(
                @Query("account") String account,
                @QueryMap Map<String, String> signMap);

        @POST("user/findUserF")
        Call<OtherUserInfo> getUserByID(
                @Query("userid") String userID,
                @QueryMap Map<String, String> signMap);

        @POST("user/findByContact")
        Call<ContantUsers> findContactUser(
                @Query(value = "mobiles", encoded = true) String mpbiles,
                @QueryMap Map<String, String> signMap);
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
                @Query("clientmac") String clientMAC,
                @QueryMap Map<String, String> signMap);

        @POST("applogin/updatePassF")
        Call<OnlySuccess> changePassword(
                @Query("account") String account,
                @Query("password") String password);

        @POST("applogin/loginF")
        Call<Login> login(
                @Query("account") String account,
                @Query("password") String password,
                @QueryMap Map<String, String> signMap);

        @POST("user/updateFieldF")
        Call<OnlySuccess> changeUserInfo(
                @Query("field") String field,
                @Query(value = "value", encoded = true) String value,
                @Query("userid") String userID,
                @QueryMap Map<String, String> signMap);


    }

}


















