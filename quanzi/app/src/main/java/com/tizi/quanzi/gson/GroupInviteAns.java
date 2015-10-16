package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/10/16.
 * 同意邀请后的信息
 */
public class GroupInviteAns extends OnlySuccess {


    /**
     * memlist : [{"id":"HTDM004874edfffea2fe466a84f9399ee50d34c3","birthday":"2015-11-15","icon":"http://ac-hy5srahi.clouddn.com/9xwI0kHt6otjmJrXaXoeOCD.jpeg","sex":"0","area":null,"name":"西瓜"},{"id":"HTDM00483c6c1fd446254a99a9381dab49f08d07","birthday":"2015-10-15","icon":"http://ac-hy5srahi.clouddn.com/hNrgmQboDu8Q4vhH40WNAGCDBR3498vqtXgbaZ8E.jpg?imageView/1/w/200/h/200/q/100/format/png","sex":"1","area":null,"name":"忘了爱"}]
     * groups : [{"id":"HTDM00483c553040acbf4a61bcecb74aee3515da","groupNo":"","groupName":"猫","notice":"铲屎官","type":"0","icon":"http://ac-hy5srahi.clouddn.com/P2CrHIIGCLtvKg9xJ4v63hi8amLqIyYixu9avaSr.jpg?imageView/1/w/200/h/200/q/100/format/png","bg":null,"remark":null,"validation":"Y","allowMatch":null,"convId":"561f73d1ddb24819b7e9a35a","createUser":"HTDM004874edfffea2fe466a84f9399ee50d34c3"}]
     */

    @SerializedName("memlist")
    public List<MemlistEntity> memlist;
    @SerializedName("groups")
    public List<Login.GroupEntity> groups;

    public static class MemlistEntity {
        /**
         * id : HTDM004874edfffea2fe466a84f9399ee50d34c3
         * birthday : 2015-11-15
         * icon : http://ac-hy5srahi.clouddn.com/9xwI0kHt6otjmJrXaXoeOCD.jpeg
         * sex : 0
         * area : null
         * name : 西瓜
         */

        @SerializedName("id")
        public String id;
        @SerializedName("birthday")
        public String birthday;
        @SerializedName("icon")
        public String icon;
        @SerializedName("sex")
        public String sex;
        @SerializedName("area")
        public Object area;
        @SerializedName("name")
        public String name;
    }

}
