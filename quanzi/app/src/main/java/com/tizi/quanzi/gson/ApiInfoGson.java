package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/9/16.
 */
public class ApiInfoGson extends OnlySuccess {


    /**
     * info : {"android_version":"1.3","time":"1442994034704","ios_version":"1.3","url":"http://www.baidu.com"}
     */

    @SerializedName("info")
    public InfoEntity info;

    public static class InfoEntity {
        /**
         * android_version : 1.3
         * time : 1442994034704
         * ios_version : 1.3
         * url : http://www.baidu.com
         */

        @SerializedName("android_version")
        public String androidVersion;
        @SerializedName("time")
        public String time;
        @SerializedName("ios_version")
        public String iosVersion;
        @SerializedName("url")
        public String url;
    }
}
