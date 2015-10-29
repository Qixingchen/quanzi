package com.tizi.quanzi.network;

import com.google.gson.Gson;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.tool.Tool;

import java.util.ArrayList;

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

        ArrayList<TAG> tags = new ArrayList<>();
        for (AllTags.TagsEntity oldtag : oldTags) {
            tags.add(new TAG(oldtag.id, oldtag.tagName));
        }

        String encodedTAG = Tool.getUTF_8String(new Gson().toJson(tags));
        groupSer.addGroup(GroupName, encodedTAG, icon, Tool.getUTF_8String(notice),
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
        groupSer.queryGroup(GroupID).enqueue(new Callback<GroupAllInfo>() {
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


    @SuppressWarnings("unchecked")
    @Override
    public GroupSetting setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }

    class TAG {
        public String tagid;
        public String tagName;

        public TAG(String tagid, String tagName) {
            this.tagid = tagid;
            this.tagName = tagName;
        }
    }
}
