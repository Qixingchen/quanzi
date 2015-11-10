package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/9/3.
 */
public class OtherUserInfo extends OnlySuccess implements Parcelable {


    public static final Creator<OtherUserInfo> CREATOR = new Creator<OtherUserInfo>() {
        public OtherUserInfo createFromParcel(Parcel source) {
            return new OtherUserInfo(source);
        }

        public OtherUserInfo[] newArray(int size) {
            return new OtherUserInfo[size];
        }
    };
    /**
     * id : HTDM00486dd5ad1afaa044658d118889c2bdc202
     * icon : http://ac-hy5srahi.clouddn.com/MnZUnRS6mu6YrowIBdikNpfZq3DKgpdTrdROzwGa.jpg?imageView/1/w/200/h/200/q/100/format/png
     * birthday : 2010-12-3
     * sex : 1
     * area : FujianXiamen
     * signatrue : 2333
     * bg : null
     * userName : 星辰
     */

    @SerializedName("id")
    public String id;
    @SerializedName("icon")
    public String icon;
    @SerializedName("birthday")
    public String birthday;
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
        this.birthday = in.readString();
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
        dest.writeString(this.birthday);
        dest.writeString(this.sex);
        dest.writeString(this.area);
        dest.writeString(this.signatrue);
        dest.writeString(this.bg);
        dest.writeString(this.userName);
    }
}
