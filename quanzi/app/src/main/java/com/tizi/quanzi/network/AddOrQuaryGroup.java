package com.tizi.quanzi.network;

import com.google.gson.Gson;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.tool.Tool;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/8/17.
 * 请求一个新群
 *
 * @see GroupClass
 * @see Group
 * @see com.tizi.quanzi.network.RetrofitAPI.Group
 */
public class AddOrQuaryGroup extends RetrofitNetworkAbs {

    private RetrofitAPI.Group groupService = RetrofitNetwork.retrofit.create(RetrofitAPI.Group.class);

    public static AddOrQuaryGroup getNewInstance() {
        return new AddOrQuaryGroup();
    }

    @SuppressWarnings("unchecked")
    @Override
    public AddOrQuaryGroup setNetworkListener(NetworkListener networkListener) {
        return super.setNetworkListener(networkListener, this);
    }

    /**
     * 创建一个群
     *
     * @param GroupName 群名
     * @param icon      群头像
     * @param notice    群公告
     * @param tag       群标签
     */
    public void NewAGroup(String GroupName, String icon, String notice,
                          ArrayList<AllTags.TagsEntity> tags, String convid) {

        String encodedTAG = Tool.getUTF_8String(new Gson().toJson(tags));
        groupService.addGroup(GroupName, encodedTAG, icon, Tool.getUTF_8String(notice),
                AppStaticValue.getUserID(), convid).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Response<Group> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
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
        final Call<GroupAllInfo> groupInfo = groupService.queryGroup(GroupID);

        groupInfo.enqueue(new Callback<GroupAllInfo>() {
            @Override
            public void onResponse(Response<GroupAllInfo> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });

    }
}
