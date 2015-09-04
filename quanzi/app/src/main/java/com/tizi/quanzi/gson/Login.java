package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/7/21.
 * 登陆
 */
public class Login {

    /**
     * msg : null
     * success : true
     * user :
     */

    @SerializedName("msg")
    private Object msg;
    @SerializedName("success")
    private boolean success;
    @SerializedName("user")
    private UserEntity user;
    @SerializedName("group")
    private List<GroupEntity> group;

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setGroup(List<GroupEntity> group) {
        this.group = group;
    }

    public Object getMsg() {
        return msg;
    }

    public boolean getSuccess() {
        return success;
    }

    public UserEntity getUser() {
        return user;
    }

    public List<GroupEntity> getGroup() {
        return group;
    }

    public static class UserEntity {
        /**
         * id : HTDM004825b32141fe9c41f09846e85f0902f0bd
         * birthday : null
         * icon : http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg
         * sex : 1
         * area : null
         * token : HTDM004887328d430d664e2d9396c88216bac375
         * groupNum : 6
         * bg : null
         * userName : 232
         * account : 1
         * signature : null
         * mobile : 1
         */

        @SerializedName("id")
        private String id;
        @SerializedName("birthday")
        private Object birthday;
        @SerializedName("icon")
        private String icon;
        @SerializedName("sex")
        private String sex;
        @SerializedName("area")
        private Object area;
        @SerializedName("token")
        private String token;
        @SerializedName("groupNum")
        private String groupNum;
        @SerializedName("bg")
        private Object bg;
        @SerializedName("userName")
        private String userName;
        @SerializedName("account")
        private String account;
        @SerializedName("signature")
        private Object signature;
        @SerializedName("mobile")
        private String mobile;

        public void setId(String id) {
            this.id = id;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setGroupNum(String groupNum) {
            this.groupNum = groupNum;
        }

        public void setBg(Object bg) {
            this.bg = bg;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setSignature(Object signature) {
            this.signature = signature;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getId() {
            return id;
        }

        public Object getBirthday() {
            return birthday;
        }

        public String getIcon() {
            return icon;
        }

        public String getSex() {
            return sex;
        }

        public Object getArea() {
            return area;
        }

        public String getToken() {
            return token;
        }

        public String getGroupNum() {
            return groupNum;
        }

        public Object getBg() {
            return bg;
        }

        public String getUserName() {
            return userName;
        }

        public String getAccount() {
            return account;
        }

        public Object getSignature() {
            return signature;
        }

        public String getMobile() {
            return mobile;
        }
    }

    public static class GroupEntity {
        /**
         * id : HTDM0048f4c2d7967c6f4011b69aaaa7af17b4e2
         * groupNo :
         * groupName : 圈子
         * notice : 圈子1
         * type : 0
         * convId : 55dea61a00b0afd40404a73d
         * icon : http://ac-hy5srahi.clouddn.com/fnwlHqNa7BPXdAfs8bZAFID.jpeg
         * bg : null
         * remark : null
         * validation : Y
         * createUser : HTDM004825b32141fe9c41f09846e85f0902f0bd
         */

        @SerializedName("id")
        private String id;
        @SerializedName("groupNo")
        private String groupNo;
        @SerializedName("groupName")
        private String groupName;
        @SerializedName("notice")
        private String notice;
        @SerializedName("type")
        private String type;
        @SerializedName("convId")
        private String convId;
        @SerializedName("icon")
        private String icon;
        @SerializedName("bg")
        private Object bg;
        @SerializedName("remark")
        private Object remark;
        @SerializedName("validation")
        private String validation;
        @SerializedName("createUser")
        private String createUser;

        public void setId(String id) {
            this.id = id;
        }

        public void setGroupNo(String groupNo) {
            this.groupNo = groupNo;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setConvId(String convId) {
            this.convId = convId;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setBg(Object bg) {
            this.bg = bg;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public void setValidation(String validation) {
            this.validation = validation;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getId() {
            return id;
        }

        public String getGroupNo() {
            return groupNo;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getNotice() {
            return notice;
        }

        public String getType() {
            return type;
        }

        public String getConvId() {
            return convId;
        }

        public String getIcon() {
            return icon;
        }

        public Object getBg() {
            return bg;
        }

        public Object getRemark() {
            return remark;
        }

        public String getValidation() {
            return validation;
        }

        public String getCreateUser() {
            return createUser;
        }
    }
}
