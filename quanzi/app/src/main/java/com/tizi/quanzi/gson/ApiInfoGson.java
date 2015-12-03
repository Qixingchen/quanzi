package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/9/16.
 */
public class ApiInfoGson extends OnlySuccess {

    /**
     * android_version : 1.0
     * content : 产品狗再次亮瞎你的钛金xx眼,升级升级要升级,保证你再也不想降回来!乖,听话。狠狠地搓一下[立刻马上]
     * time : 1449130939605
     * ios_version : 1.0
     * url : http://www.baidu.com
     */

    @SerializedName("info")
    public InfoEntity info;

    public static class InfoEntity {
        @SerializedName("android_version")
        public String androidVersion;
        @SerializedName("content")
        public String content;
        @SerializedName("time")
        public String time;
        @SerializedName("ios_version")
        public String iosVersion;
        @SerializedName("url")
        public String url;
    }
}
