package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * tags : 马达加斯加的企鹅,小时代,权利的游戏,何以笙箫默,Allen Iverson
     * id : HTDM004895dfca0c4c404097a8d3150df70b0d19
     * icon : https://dn-hy5srahi.qbox.me/o77Vt6BYzGhvpGhowjY0RQB.jpeg
     * birthday : 2013-09-21
     * sex : 1
     * area : 福建省厦门市
     * signatrue : 双十一倒计时，密码已全改…乐扣乐扣看看咯了吗？？？？？？？？？？？？？ 我想问一下 我想想
     * bg : http://ac-hy5srahi.clouddn.com/VtOhVLNmEonIlG78lXPQJ2A.jpeg
     * userName : zkw
     */

    @SerializedName("tags")
    public String tags;
    @SerializedName("id")
    public String id;
    @SerializedName("icon")
    public String icon;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("sex")
    public int sex;
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
        this.tags = in.readString();
        this.id = in.readString();
        this.icon = in.readString();
        this.birthday = in.readString();
        this.sex = in.readInt();
        this.area = in.readString();
        this.signatrue = in.readString();
        this.bg = in.readString();
        this.userName = in.readString();
    }

    public List<String> getTags() {
        if (tags != null) {
            return Arrays.asList(tags.split(","));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tags);
        dest.writeString(this.id);
        dest.writeString(this.icon);
        dest.writeString(this.birthday);
        dest.writeInt(this.sex);
        dest.writeString(this.area);
        dest.writeString(this.signatrue);
        dest.writeString(this.bg);
        dest.writeString(this.userName);
    }
}
