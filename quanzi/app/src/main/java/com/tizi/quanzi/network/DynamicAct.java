package com.tizi.quanzi.network;

import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

import retrofit.Callback;

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

        dynsSer.findDyns(themeID, groupID, start, StaticField.MessageQueryLimit.DynamicLimit, Tool.getSignMap())
                .enqueue(new Callback<com.tizi.quanzi.gson.Dyns>() {
                    @Override
                    public void onResponse(retrofit.Response<com.tizi.quanzi.gson.Dyns> response) {
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
    public DynamicAct setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
    }

}
