package com.tizi.quanzi.gson;

import java.util.List;

/**
 * Created by qixingchen on 15/7/21.
 */
public class Login {
    private List<GroupEntity> group;
    private UserEntity user;

    public void setGroup(List<GroupEntity> group) {
        this.group = group;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<GroupEntity> getGroup() {
        return group;
    }

    public UserEntity getUser() {
        return user;
    }

    public static class GroupEntity {
        private String id;
        private String icon;
        private String groupName;
        private String type;

        public void setId(String id) {
            this.id = id;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getType() {
            return type;
        }
    }

    public static class UserEntity {
        private String id;
        private String icon;
        private String sex;
        private String area;
        private String token;
        private String account;
        private String userName;
        private String bg;
        private String signatrue;
        private String groupNum;
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

        public void setSignatrue(String signatrue) {
            this.signatrue = signatrue;
        }

        public void setGroupNum(String groupNum) {
            this.groupNum = groupNum;
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

        public String getSignatrue() {
            return signatrue;
        }

        public String getGroupNum() {
            return groupNum;
        }

        public String getMobile() {
            return mobile;
        }
    }

}
