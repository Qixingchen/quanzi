package com.tizi.quanzi.gson;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by qixingchen on 15/7/21.
 * 登陆
 */
public class Login {


    /**
     * success : true
     * msg : null
     */
    private List<GroupEntity> group;
    private UserEntity user;
    private boolean success;
    private String msg;

    public void setGroup(List<GroupEntity> group) {
        this.group = group;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<GroupEntity> getGroup() {
        return group;
    }

    public UserEntity getUser() {
        return user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public static class GroupEntity {
        /**
         * groupNo :
         * id : HTDM00483b2b955468cf4c7ba0430c7bf0994c70
         * icon : http://ac-hy5srahi.clouddn.com/EuzALiJGgUTQTOeewEmMMRD.jpeg
         * createUser : HTDM0048efd1df5cacd448e48f86775e454e1981
         * groupName : 我的圈子
         * validation : Y
         * remark : null
         * convId : 55dc2aa560b27e6cd4e34dec
         * bg : null
         * type : 0
         * notice : 第一个
         */
        private String groupNo;
        private String id;
        private String icon;
        private String createUser;
        private String groupName;
        private boolean validation;
        private String remark;
        private String convId;
        private String bg;
        private String type;
        private String notice;

        public void setGroupNo(String groupNo) {
            this.groupNo = groupNo;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public void setValidation(String validation) {
            this.validation = (validation.compareTo("Y") == 0);
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setConvId(String convId) {
            this.convId = convId;
        }

        public void setBg(String bg) {
            this.bg = bg;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public String getGroupNo() {
            return groupNo;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getCreateUser() {
            return createUser;
        }

        public String getGroupName() {
            return groupName;
        }

        public boolean getValidation() {
            return validation;
        }

        public String getRemark() {
            return remark;
        }

        public String getConvId() {
            return convId;
        }

        public String getBg() {
            return bg;
        }

        public String getType() {
            return type;
        }

        public String getNotice() {
            return notice;
        }
    }

    public static class UserEntity {
        /**
         * id : HTDM0048efd1df5cacd448e48f86775e454e1981
         * icon : http://ac-hy5srahi.clouddn.com/q3fLSLIwTbnj8PPM9aJpRKD.png
         * sex : 0
         * area : null
         * token : HTDM0048b1c89a47da954a089e49005bcda9d617
         * account : 1
         * userName : 1
         * bg : null
         * groupNum : 6
         * signature : null
         * mobile : 1
         */
        private String id;
        private String icon;
        private String sex;
        private String area;
        private String token;
        private String account;
        private String userName;
        private String bg;
        private String groupNum;
        private String signature;
        private String mobile;

        public void setId(String id) {
            this.id = id;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setBg(String bg) {
            this.bg = bg;
        }

        public void setGroupNum(String groupNum) {
            this.groupNum = groupNum;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getSex() {
            return sex;
        }

        public String getArea() {
            return area;
        }

        public String getToken() {
            return token;
        }

        public String getAccount() {
            return account;
        }

        public String getUserName() {
            return userName;
        }

        public String getBg() {
            return bg;
        }

        public String getGroupNum() {
            return groupNum;
        }

        public String getSignature() {
            return signature;
        }

        public String getMobile() {
            return mobile;
        }
    }
}
