package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.OnlySuccess;

import retrofit.Callback;
import retrofit.Response;

/**
 * Created by qixingchen on 15/9/25.
 */
public class Dyns extends RetrofitNetworkAbs {

    private RetrofitAPI.Dyns dynsSer = RetrofitNetwork.retrofit.create(RetrofitAPI.Dyns.class);

    public static Dyns getNewInstance() {
        return new Dyns();
    }

    /*获取评论*/
    public void getComment(String dynID, int start, int limit) {
        dynsSer.findComment(dynID, start, limit).enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Response<Comments> response) {
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
        dynsSer.addComent(dynID, comment).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Response<OnlySuccess> response) {
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
        dynsSer.addComent(dynID, comment, replyID, atUserID).enqueue(new Callback<OnlySuccess>() {
            @Override
            public void onResponse(Response<OnlySuccess> response) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }


    public Dyns setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }
}
