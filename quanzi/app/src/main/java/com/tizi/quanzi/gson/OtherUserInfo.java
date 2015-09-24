package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/9/3.
 */
public class OtherUserInfo extends OnlySuccess implements Parcelable {


    public static final Parcelable.Creator<OtherUserInfo> CREATOR = new Parcelable.Creator<OtherUserInfo>() {
        public OtherUserInfo createFromParcel(Parcel source) {
            return new OtherUserInfo(source);
        }

        public OtherUserInfo[] newArray(int size) {
            return new OtherUserInfo[size];
        }
    };
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
    public String area;
    @SerializedName("signatrue")
    public String signatrue;
    @SerializedName("bg")
    public String bg;
    @SerializedName("userName")
    public String userName;

    public OtherUserInfo() {
    }

    protected OtherUserInfo(Parcel in) {
        this.id = in.readString();
        this.icon = in.readString();
        this.sex = in.readString();
        this.area = in.readString();
        this.signatrue = in.readString();
        this.bg = in.readString();
        this.userName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.icon);
        dest.writeString(this.sex);
        dest.writeString(this.area);
        dest.writeString(this.signatrue);
        dest.writeString(this.bg);
        dest.writeString(this.userName);
    }
}
