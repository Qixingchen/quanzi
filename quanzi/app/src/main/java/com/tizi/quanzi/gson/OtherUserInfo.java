package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/9/3.
 */
public class OtherUserInfo extends OnlySuccess {


    /**
     * id : HTDM004825b32141fe9c41f09846e85f0902f0bd
     * icon : http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg
     * sex : 1
     * area : null
     * signatrue : null
     * bg : null
     * userName : 232
     * success : true
     */

    @SerializedName("id")
    public String id;
    @SerializedName("icon")
    public String icon;
    @SerializedName("sex")
    public String sex;
    @SerializedName("area")
    public Object area;
    @SerializedName("signatrue")
    public Object signatrue;
    @SerializedName("bg")
    public Object bg;
    @SerializedName("userName")
    public String userName;
}
