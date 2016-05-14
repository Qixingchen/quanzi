package com.tizi.quanzi.network;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.gson.UserTags;
import com.tizi.quanzi.tool.Tool;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qixingchen on 15/9/9.
 * 群设置管理－后台
 *
 * @see com.tizi.quanzi.network.RetrofitAPI.Group
 */
public class GroupSetting extends RetrofitNetworkAbs {


    private RetrofitAPI.Group groupSer = RetrofitNetwork.retrofit.create(RetrofitAPI.Group.class);

    public static GroupSetting getNewInstance() {
        return new GroupSetting();
    }

    /**
     * 更改群的信息
     *
     * @param groupID 群ID
     * @param field   需更改的字段名称
     * @param value   更改值
     */
    private void changeField(String groupID, String field, String value) {

        groupSer.changeGroupInfo(groupID, field, Tool.getUTF_8String(value)).enqueue(new Callback<OnlySuccess>() {
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
     * 更改群名
     *
     * @param groupID 群ID
     * @param name    更改为的群名
     */
    public void ChangeName(String groupID, String name) {
        changeField(groupID, "groupName", name);
    }

    /**
     * 更改群签名
     *
     * @param groupID 群ID
     * @param sign    更改为的群签名
     */
    public void ChangeSign(String groupID, String sign) {
        changeField(groupID, "notice", sign);
    }

    /**
     * 更改群头像
     *
     * @param groupID 群ID
     * @param uri     更改的头像uri
     */
    public void changeIcon(String groupID, String uri) {
        changeField(groupID, "icon", uri);
    }

    /**
     * 更改群标签
     *
     * @param groupID 群ID
     * @param oldTags 更改的 TAGS
     */
    public void changeTags(String groupID, ArrayList<AllTags.TagsEntity> oldTags) {
        changeField(groupID, "grouptags", new AllTags().getTagServerString(oldTags));
    }

    /**
     * 查找所有TAG
     */
    public void findAllTags(boolean isGroup) {
        groupSer.queryAllAvailableTag(isGroup ? 0 : 1).enqueue(new Callback<AllTags>() {
            @Override
            public void onResponse(Call<AllTags> call, Response<AllTags> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<AllTags> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 查看用户的标签
     *
     * @param userID 要查询的UserID
     */
    public void findUserTags(String userID) {
        groupSer.queryUserTag(userID).enqueue(new Callback<UserTags>() {
            @Override
            public void onResponse(Call<UserTags> call, Response<UserTags> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<UserTags> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /**
     * 创建一个群
     *
     * @param GroupName 群名
     * @param icon      群头像
     * @param notice    群公告
     * @param oldTags   群标签
     */
    public void NewAGroup(String GroupName, String icon, String notice,
                          ArrayList<AllTags.TagsEntity> oldTags, String convid) {
        Call<Group> groupCall;
        String userid = AppStaticValue.getUserID();
        if (oldTags.size() == 0) {
            groupCall = groupSer.addGroup(userid, GroupName, icon, notice, convid);

        } else {
            String encodedTAG = Tool.getUTF_8String(new AllTags().getTagServerString(oldTags));
            groupCall = groupSer.addGroup(userid, GroupName, encodedTAG, icon, notice, convid);
        }

        groupCall.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                myOnFailure(t);
            }
        });
    }


    /**
     * 查询群信息
     * 通过回调返回值
     *
     * @param GroupID 群ID
     */
    public void queryGroup(String GroupID) {
        groupSer.queryGroup(GroupID).enqueue(new Callback<GroupAllInfo>() {
            @Override
            public void onResponse(Call<GroupAllInfo> call, Response<GroupAllInfo> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Call<GroupAllInfo> call, Throwable t) {
                myOnFailure(t);
            }
        });

    }


    @SuppressWarnings("unchecked")
    @Override
    public GroupSetting setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
