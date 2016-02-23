package com.tizi.chatlibrary.staticData;

/**
 * Created by qixingchen on 16/2/23.
 * 当前用户基础信息
 */
@Deprecated
public class UserInfo {

    private static UserInfo mInstance;
    private String userID;
    private String userName;
    private String userIcon;

    public static UserInfo getInstance() {
        if (mInstance == null) {
            synchronized (UserInfo.class) {
                if (mInstance == null) {
                    mInstance = new UserInfo();
                }
            }
        }
        return mInstance;
    }

    public void setUserInfo(String userID, String userName, String userIcon) {
        this.userID = userID;
        this.userName = userName;
        this.userIcon = userIcon;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
