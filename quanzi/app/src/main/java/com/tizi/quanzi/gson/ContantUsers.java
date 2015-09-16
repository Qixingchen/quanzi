package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/9/16.
 * 通讯录好友
 */
public class ContantUsers extends OnlySuccess {


    /**
     * mobiles : [{"icon":"http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg","userId":"HTDM004825b32141fe9c41f09846e85f0902f0bd","userName":"232","mobile":"1"}]
     */

    @SerializedName("mobiles")
    public List<MobilesEntity> mobiles;

    public static class MobilesEntity {
        /**
         * icon : http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg
         * userId : HTDM004825b32141fe9c41f09846e85f0902f0bd
         * userName : 232
         * mobile : 1
         */

        @SerializedName("icon")
        public String icon;
        @SerializedName("userId")
        public String userId;
        @SerializedName("userName")
        public String userName;
        @SerializedName("mobile")
        public String mobile;
    }
}
