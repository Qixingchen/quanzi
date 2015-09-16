package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;

/**
 * Created by qixingchen on 15/9/9.
 * 群设置管理－后台
 */
public class GroupSetting extends RetrofitNetworkAbs {


    public static GroupSetting getInstance() {
        return new GroupSetting();
    }

    private RetrofitAPI.Group groupSer = RetrofitNetwork.retrofit.create(RetrofitAPI.Group.class);

    private void changeField(String groupID, String field, String value) {

        groupSer.changeGroupInfo(groupID, field, value, Tool.getSignMap()).enqueue(new Callback<OnlySuccess>() {
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

    public void ChangeName(String groupID, String name) {
        changeField(groupID, "groupName", name);
    }

    public void changeIcon(String groupID, String uri) {
        changeField(groupID, "icon", uri);
    }

    @SuppressWarnings("unchecked")
    @Override
    public GroupSetting setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
