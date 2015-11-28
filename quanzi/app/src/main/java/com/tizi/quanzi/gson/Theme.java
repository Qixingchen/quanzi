package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 * 主题活动
 */
public class Theme extends OnlySuccess implements Parcelable {


    public static final Parcelable.Creator<Theme> CREATOR = new Parcelable.Creator<Theme>() {
        public Theme createFromParcel(Parcel source) {
            return new Theme(source);
        }

        public Theme[] newArray(int size) {
            return new Theme[size];
        }
    };
    /**
     * id : HTDM004832d9e7d1827b48d89845204dc5139421
     * title : 脱单大作战
     * content : #帮基蜜脱单#从这里开始~!
     * icon : http://ac-hy5srahi.clouddn.com/c815654724ba000f.png
     * detailUrl : http://v.xiumi.us/stage/v3/1ZhdA/4165144
     * template : 1
     * signNum : 1004
     * beginDate : null
     * endDate : null
     * adUrl : null
     * beginTime : 12:00:00
     * endTime : 16:00:00
     */

    @SerializedName("acts")
    public List<ActsEntity> acts;

    public Theme() {
    }

    protected Theme(Parcel in) {
        this.acts = new ArrayList<ActsEntity>();
        in.readList(this.acts, List.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.acts);
    }

    public static class ActsEntity implements Parcelable {
        public static final Creator<ActsEntity> CREATOR = new Creator<ActsEntity>() {
            public ActsEntity createFromParcel(Parcel source) {
                return new ActsEntity(source);
            }

            public ActsEntity[] newArray(int size) {
                return new ActsEntity[size];
            }
        };
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public String content;
        @SerializedName("icon")
        public String icon;
        @SerializedName("detailUrl")
        public String detailUrl;
        @SerializedName("template")
        public int template;
        @SerializedName("signNum")
        public int signNum;
        @SerializedName("beginDate")
        public String beginDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("adUrl")
        public String adUrl;
        @SerializedName("beginTime")
        public String beginTime;
        @SerializedName("endTime")
        public String endTime;

        public ActsEntity() {
        }

        protected ActsEntity(Parcel in) {
            this.id = in.readString();
            this.title = in.readString();
            this.content = in.readString();
            this.icon = in.readString();
            this.detailUrl = in.readString();
            this.template = in.readInt();
            this.signNum = in.readInt();
            this.beginDate = in.readString();
            this.endDate = in.readString();
            this.adUrl = in.readString();
            this.beginTime = in.readString();
            this.endTime = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.content);
            dest.writeString(this.icon);
            dest.writeString(this.detailUrl);
            dest.writeInt(this.template);
            dest.writeInt(this.signNum);
            dest.writeString(this.beginDate);
            dest.writeString(this.endDate);
            dest.writeString(this.adUrl);
            dest.writeString(this.beginTime);
            dest.writeString(this.endTime);
        }
    }
}
