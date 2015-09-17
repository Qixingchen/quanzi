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

    /**
     * 更改群的信息
     *
     * @param groupID 群ID
     * @param field   需更改的字段名称
     * @param value   更改值
     */
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
     * 更改群头像
     *
     * @param groupID 群ID
     * @param uri     更改的头像uri
     */
    public void changeIcon(String groupID, String uri) {
        changeField(groupID, "icon", uri);
    }

    @SuppressWarnings("unchecked")
    @Override
    public GroupSetting setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
