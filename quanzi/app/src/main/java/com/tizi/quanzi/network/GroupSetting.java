package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
            public void onResponse(retrofit.Response<OnlySuccess> response, Retrofit retrofit) {
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

    /**
     * 查找所有TAG
     */
    public void findAllTags() {
        groupSer.queryAllAvailableTag().enqueue(new Callback<AllTags>() {
            @Override
            public void onResponse(Response<AllTags> response, Retrofit retrofit) {
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
    public GroupSetting setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
