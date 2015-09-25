package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 * 热门动态
 */
public class HotDyns extends OnlySuccess {


    /**
     * msg : null
     * success : true
     */

    @SerializedName("dyns")
    public List<Dyns.DynsEntity> dyns;

}
