package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.AddComment;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by qixingchen on 15/8/18.
 * 动态查询
 *
 * @see RetrofitAPI.Dyns
 */
public class DynamicAct extends RetrofitNetworkAbs {

    private RetrofitAPI.Dyns dynsSer = RetrofitNetwork.retrofit.create(RetrofitAPI.Dyns.class);

    public static DynamicAct getNewInstance() {
        return new DynamicAct();
    }

    /**
     * 查询圈子内对于某事件的动态
     */
    public void getGroupDynamic(String themeID, String groupID, int start) {

        dynsSer.findDyns(themeID, groupID, start, StaticField.QueryLimit.DynamicLimit)
                .enqueue(new Callback<com.tizi.quanzi.gson.Dyns>() {
                    @Override
                    public void onResponse(retrofit.Response<com.tizi.quanzi.gson.Dyns> response, Retrofit retrofit) {
                        myOnResponse(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        myOnFailure(t);
                    }
                });
    }

    /**
     * 获取动态
     *
     * @param isGroup 查群还是查主题
     * @param id      群或主题的ID
     * @param start   开始的序号
     */
    public void getGroupDynamic(boolean isGroup, String id, int start) {

        Call<com.tizi.quanzi.gson.Dyns> dynsCall;

        if (isGroup) {
            dynsCall = dynsSer.findGroupDyns(id, start, StaticField.QueryLimit.DynamicLimit);
        } else {
            dynsCall = dynsSer.findThemeDyns(id, start, StaticField.QueryLimit.DynamicLimit);
        }

        dynsCall.enqueue(new Callback<com.tizi.quanzi.gson.Dyns>() {
            @Override
            public void onResponse(retrofit.Response<com.tizi.quanzi.gson.Dyns> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }


    /*获取评论*/
    public void getComment(String dynID, int start, int limit) {
        dynsSer.findComment(dynID, start, limit).enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Response<Comments> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /*添加评论*/
    public void addComment(String dynID, String comment) {
        dynsSer.addComent(dynID, Tool.getUTF_8String(comment)).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Response<OnlySuccess> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }


    /*添加评论*/
    public void addComment(String dynID, String comment, String replyID, String atUserID) {
        dynsSer.addComent(dynID, Tool.getUTF_8String(comment), replyID, atUserID).enqueue(new Callback<AddComment>() {
            @Override
            public void onResponse(Response<AddComment> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /*发表动态*/
    public void addDYn(String ThemeID, String GroupID, String comment, String pic) {

        Call<OnlySuccess> addDynCall;
        comment = Tool.getUTF_8String(comment);
        if (pic == null) {
            addDynCall = dynsSer.addDyn(ThemeID, GroupID, comment);
        } else {
            Log.i(TAG, pic);
            addDynCall = dynsSer.addDyn(ThemeID, GroupID, comment, Tool.getUTF_8String(pic));
        }

        addDynCall.enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Response<OnlySuccess> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    public void addDYn(String ThemeID, String GroupID, String comment) {
        addDYn(ThemeID, GroupID, comment, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DynamicAct setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }

}
