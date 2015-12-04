package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/9/16.
 */
public class ApiInfoGson extends OnlySuccess {

    /**
     * android_version : 0.2.6
     * content : 产品狗再次亮瞎你的钛金xx眼,升级升级要升级,保证你再也不想降回来!乖,听话。狠狠地搓一下[立刻马上]
     * android_url : https://dn-hy5srahi.qbox.me/8b1e3ed3d6f00c68.apk
     * time : 1449194511052
     * ios_version : 1.0
     * ios_url : http://www.baidu.com
     */

    @SerializedName("info")
    public InfoEntity info;

    public static class InfoEntity {
        @SerializedName("android_version")
        public String androidVersion;
        @SerializedName("content")
        public String content;
        @SerializedName("android_url")
        public String androidUrl;
        @SerializedName("time")
        public String time;
        @SerializedName("ios_version")
        public String iosVersion;
        @SerializedName("ios_url")
        public String iosUrl;
    }
}
