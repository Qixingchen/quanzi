package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class Dyns extends OnlySuccess {


    /**
     * icon : http://ac-hy5srahi.clouddn.com/lQ4xXWF1UO6oiXxSw9Rt4HOtv6qE9WRdL6Ok2cRS.jpg?imageView/1/w/200/h/200/q/100/format/png
     * createTime : 20151107174409
     * sex : 1
     * groupName : 随变
     * senderId : HTDM00484ef4c47c7f044c11b8956f3fe5374ceb
     * content : 英语英语
     * commentNum : 0
     * createUser : HTDM0048eb9fbba903444b53b372de62b0f0c21f
     * pics : [{"url":"http://ac-hy5srahi.clouddn.com/JT1dG4uWNK4GZTZGzS70lwkow2EzpEBLniapVmYM.gif"}]
     * groupIcon : http://ac-hy5srahi.clouddn.com/C9suz7o2cCsQRzgUYi0SHaQg236MwgGM4aAbqmE5.jpg
     * nickName : 不见
     * zan : 1
     * dynid : HTDM0048e8ec4429348441d5896a9f2d5252adde
     */

    @SerializedName("dyns")
    public List<DynsEntity> dyns;

    public static class DynsEntity implements Parcelable {
        public static final Parcelable.Creator<DynsEntity> CREATOR = new Parcelable.Creator<DynsEntity>() {
            public DynsEntity createFromParcel(Parcel source) {
                return new DynsEntity(source);
            }

            public DynsEntity[] newArray(int size) {
                return new DynsEntity[size];
            }
        };
        @SerializedName("icon")
        public String icon;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("sex")
        public String sex;
        @SerializedName("groupName")
        public String groupName;
        @SerializedName("senderId")
        public String senderId;
        @SerializedName("content")
        public String content;
        @SerializedName("commentNum")
        public int commentNum;
        @SerializedName("createUser")
        public String createUser;
        @SerializedName("groupIcon")
        public String groupIcon;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("zan")
        public int zan;
        @SerializedName("dynid")
        public String dynid;
        /**
         * url : http://ac-hy5srahi.clouddn.com/JT1dG4uWNK4GZTZGzS70lwkow2EzpEBLniapVmYM.gif
         */

        @SerializedName("pics")
        public List<PicsEntity> pics;


        public DynsEntity() {
        }

        protected DynsEntity(Parcel in) {
            this.icon = in.readString();
            this.createTime = in.readString();
            this.sex = in.readString();
            this.groupName = in.readString();
            this.senderId = in.readString();
            this.content = in.readString();
            this.commentNum = in.readInt();
            this.createUser = in.readString();
            this.groupIcon = in.readString();
            this.nickName = in.readString();
            this.zan = in.readInt();
            this.dynid = in.readString();
            this.pics = new ArrayList<PicsEntity>();
            in.readList(this.pics, List.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.icon);
            dest.writeString(this.createTime);
            dest.writeString(this.sex);
            dest.writeString(this.groupName);
            dest.writeString(this.senderId);
            dest.writeString(this.content);
            dest.writeInt(this.commentNum);
            dest.writeString(this.createUser);
            dest.writeString(this.groupIcon);
            dest.writeString(this.nickName);
            dest.writeInt(this.zan);
            dest.writeString(this.dynid);
            dest.writeList(this.pics);
        }

        public static class PicsEntity {
            @SerializedName("url")
            public String url;
        }
    }
}
