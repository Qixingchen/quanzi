package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/9/16.
 */
public class ApiInfo extends OnlySuccess {

    @SerializedName("info")
    public InfoEntity info;

    public static class InfoEntity {
        /**
         * time : 1442370063843
         * version : 1.0
         */

        @SerializedName("time")
        public String time;
        @SerializedName("version")
        public String version;
    }
}
