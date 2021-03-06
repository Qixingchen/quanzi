package com.tizi.quanzi.gson;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.google.gson.annotations.SerializedName;
import com.tizi.quanzi.BR;

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

    public static class UserEntity implements Observable {
        @SerializedName("id")
        private String id;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("icon")
        private String icon;
        @SerializedName("sex")
        private int sex;
        @SerializedName("area")
        private String area;
        @SerializedName("token")
        private String token;
        @SerializedName("groupNum")
        private String groupNum;
        @SerializedName("bg")
        private String bg;
        @SerializedName("account")
        private String account;
        @SerializedName("signature")
        private String signature;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("userName")
        private String userName;
        private PropertyChangeRegistry pcr = new PropertyChangeRegistry();

        @Bindable
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
            pcr.notifyChange(this, BR.id);
        }

        @Bindable
        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
            pcr.notifyChange(this, BR.birthday);
        }

        @Bindable
        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
            pcr.notifyChange(this, BR.icon);
        }

        @Bindable
        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
            pcr.notifyChange(this, BR.sex);
        }

        @Bindable
        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
            pcr.notifyChange(this, BR.area);
        }

        @Bindable
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
            pcr.notifyChange(this, BR.token);
        }

        @Bindable
        public String getGroupNum() {
            return groupNum;
        }

        public void setGroupNum(String groupNum) {
            this.groupNum = groupNum;
            pcr.notifyChange(this, BR.groupNum);
        }

        @Bindable
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
            pcr.notifyChange(this, BR.userName);
        }

        @Bindable
        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
            pcr.notifyChange(this, BR.account);
        }

        @Bindable
        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
            pcr.notifyChange(this, BR.signature);
        }

        @Bindable
        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
            pcr.notifyChange(this, BR.mobile);
        }

        @Bindable
        public String getBg() {
            return bg;
        }

        public void setBg(String bg) {
            this.bg = bg;
            pcr.notifyChange(this, BR.bg);
        }

        /**
         * Adds a callback to listen for changes to the Observable.
         *
         * @param callback The callback to start listening.
         */
        @Override
        public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            pcr.add(callback);
        }

        /**
         * Removes a callback from those listening for changes.
         *
         * @param callback The callback that should stop listening.
         */
        @Override
        public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            pcr.remove(callback);
        }
    }

    public static class GroupEntity extends GroupAllInfo.GroupEntity {

        @SerializedName("memlist")
        public List<GroupAllInfo.MemberEntity> memlist;
    }
}
