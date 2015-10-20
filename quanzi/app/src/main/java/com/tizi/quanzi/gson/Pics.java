package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/10/20.
 */
public class Pics {


    /**
     * url : http://www.baidu.com/2.jpg
     */

    @SerializedName("url")
    public String url;

    public Pics(String url) {
        this.url = url;
    }
}
