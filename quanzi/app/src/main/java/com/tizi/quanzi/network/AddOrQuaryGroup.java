package com.tizi.quanzi.network;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.tool.Tool;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by qixingchen on 15/8/17.
 * 请求一个新群
 *
 * @see GroupClass
 * @see Group
 */
public class AddOrQuaryGroup extends RetrofitNetworkAbs {

    @SuppressWarnings("unchecked")
    @Override
    public AddOrQuaryGroup setNetworkListener(NetworkListener networkListener) {
        return super.setNetworkListener(networkListener, this);
    }

    private RetrofitAPI.Group groupService = RetrofitNetwork.retrofit.create(RetrofitAPI.Group.class);

    private AddOrQuaryGroup() {
    }

    public static AddOrQuaryGroup getNewInstance() {
        return new AddOrQuaryGroup();
    }


    /**
     * 创建一个群
     *
     * @param GroupName 群名
     * @param icon      群头像
     * @param notice    群公告
     * @param userID    创建者 todo 去掉
     * @param tag       群标签
     */
    public void NewAGroup(String GroupName, String icon, String notice, String userID, String tag, String convid) {

        String encodedTAG = Tool.getUTF_8String(tag);
        final Call<GroupAllInfo> addgroup = groupService.addGroup(GroupName, encodedTAG, icon, notice,
                AppStaticValue.getUserID(), convid, Tool.getSignMap());

        addgroup.enqueue(new Callback<GroupAllInfo>() {
            @Override
            public void onResponse(retrofit.Response<GroupAllInfo> response) {
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
        final Call<GroupAllInfo> groupInfo = groupService.queryGroup(GroupID, Tool.getSignMap());

        groupInfo.enqueue(new Callback<GroupAllInfo>() {
            @Override
            public void onResponse(Response<GroupAllInfo> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });

    }
}
