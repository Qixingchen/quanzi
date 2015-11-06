package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/7/21.
 * 登陆
 */
public class Login extends OnlySuccess {


    /**
     * id : HTDM0048dfb55dde7dc34f14939467870757af1b
     * birthday : 2002-7-17
     * icon : http://ac-hy5srahi.clouddn.com/6Y13Qyz4z2qPrbBm23uKLgUGxLYAaV64lmIH4wNb.jpg?imageView/1/w/200/h/200/q/100/format/png
     * sex : 0
     * area : FujianXiamen
     * token : HTDM004872b5afd166b449a19d6e9da5937d0743
     * groupNum : 3
     * bg : null
     * userName : 星辰
     * account : 18059237967
     * signature : 你好
     * mobile : 18059237967
     */

    @SerializedName("user")
    public UserEntity user;
    /**
     * id : HTDM0048f1970aba49a34c19a173979f31f3b70c
     * groupNo :
     * groupName : 6666
     * notice : 你好1111
     * type : 0
     * convId : 5620bc3e60b296e59750d737
     * icon : http://ac-hy5srahi.clouddn.com/M0z3DClgsluwza7SPiVU4K3lLfgxVsCs7mSUZtdx.jpg?imageView/1/w/200/h/200/q/100/format/png
     * bg : null
     * remark : null
     * validation : Y
     * createUser : HTDM0048dfb55dde7dc34f14939467870757af1b
     * memlist : [{"id":"HTDM0048dfb55dde7dc34f14939467870757af1b","birthday":"2002-7-17","icon":"http://ac-hy5srahi.clouddn.com/6Y13Qyz4z2qPrbBm23uKLgUGxLYAaV64lmIH4wNb.jpg?imageView/1/w/200/h/200/q/100/format/png","sex":"0","area":"FujianXiamen","name":"星辰","bg":null,"signature":"你好"}]
     */

    @SerializedName("group")
    public List<GroupEntity> group;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<GroupEntity> getGroup() {
        return group;
    }

    public void setGroup(List<GroupEntity> group) {
        this.group = group;
    }

    public static class UserEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("birthday")
        public String birthday;
        @SerializedName("icon")
        public String icon;
        @SerializedName("sex")
        public int sex;
        @SerializedName("area")
        public String area;
        @SerializedName("token")
        public String token;
        @SerializedName("groupNum")
        public String groupNum;
        @SerializedName("bg")
        public String bg;
        @SerializedName("userName")
        public String userName;
        @SerializedName("account")
        public String account;
        @SerializedName("signature")
        public String signature;
        @SerializedName("mobile")
        public String mobile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getGroupNum() {
            return groupNum;
        }

        public void setGroupNum(String groupNum) {
            this.groupNum = groupNum;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }

    public static class GroupEntity extends GroupAllInfo.GroupEntity {

        @SerializedName("memlist")
        public List<GroupAllInfo.MemberEntity> memlist;
    }
}
