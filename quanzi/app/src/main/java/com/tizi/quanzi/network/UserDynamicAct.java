package com.tizi.quanzi.network;

import com.tizi.quanzi.gson.AddComment;
import com.tizi.quanzi.gson.AddZan;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.IsZan;
import com.tizi.quanzi.gson.OnlySuccess;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Yulan on 2015/11/29.
 */
public class UserDynamicAct extends RetrofitNetworkAbs {

    private RetrofitAPI.UserDyns dynsSer = RetrofitNetwork.retrofit.create(RetrofitAPI.UserDyns.class);

    public static UserDynamicAct getNewInstance() {
        return new UserDynamicAct();
    }

    /**
     * 查询圈子内对于某事件的动态
     */
    public void getDynamic(int start) {

        dynsSer.findDyns(start, StaticField.Limit.DynamicLimit)
                .enqueue(new Callback<Dyns>() {
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
     * @param userID 想要获取动态的用户的ID
     * @param start  开始的序号
     */
    public void getDynamic(String userID, int start) {

        dynsSer.findDyns(userID, start, StaticField.Limit.DynamicLimit).enqueue(new Callback<com.tizi.quanzi.gson.Dyns>() {
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
     * @param dynID 动态ID
     */
    public void getDynamicByID(String dynID) {
        dynsSer.findDynByID(dynID).enqueue(new Callback<Dyns>() {
            @Override
            public void onResponse(Response<Dyns> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    /*点赞*/
    public void addZan(String dynID, boolean isZan) {
        dynsSer.zan(dynID, isZan ? 1 : -1).enqueue(new Callback<AddZan>() {
            @Override
            public void onResponse(Response<AddZan> response, Retrofit retrofit) {
                myOnResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }
        });
    }

    public void isZan(String dynID) {
        dynsSer.isZan(dynID).enqueue(new Callback<IsZan>() {
            @Override
            public void onResponse(Response<IsZan> response, Retrofit retrofit) {
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
        dynsSer.addComent(dynID, Tool.getUTF_8String(comment)).enqueue(new Callback<AddComment>() {
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
    public void addDYn(String comment, String pic) {

        Call<OnlySuccess> addDynCall;
        comment = Tool.getUTF_8String(comment);
        if (pic == null) {
            addDynCall = dynsSer.addDyn(comment);
        } else {
            Log.i(TAG, pic);
            addDynCall = dynsSer.addDyn(comment, Tool.getUTF_8String(pic));
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

    public void addDYn(String comment) {
        addDYn(comment, null);
    }

    /*删除评论*/
    public void deleteComment(String commentID) {
        dynsSer.deleteComment(commentID).enqueue(new Callback<OnlySuccess>() {
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

    /*删除动态*/
    public void deleteDyn(String dynID) {
        dynsSer.deleteDyn(dynID).enqueue(new Callback<OnlySuccess>() {
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

    @SuppressWarnings("unchecked")
    @Override
    public UserDynamicAct setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }

}
