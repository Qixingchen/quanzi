package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class Dyns extends OnlySuccess {


    /**
     * msg : null
     * success : true
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
        /**
         * commentNum : 0
         * content : jy
         * createTime : 20150908145122
         * icon : http://ac-hy5srahi.clouddn.com/DfoMrTPn9NMvAFtP5nPLLwA.jpeg
         * pics : [{"url":"http://ac-hy5srahi.clouddn.com/9AEsakFeTW4fJTiHnrH5JnD.jpeg"},{"url":"http://ac-hy5srahi.clouddn.com/VX7EO9O7OHRQABsh0fOJIwD.jpeg"},{"url":"http://ac-hy5srahi.clouddn.com/xlWdQaFfyXl2BEFrvaIrMLB.jpeg"}]
         * sex : 0
         * createUser : HTDM0048e1347f922f5d4d6c837ab27d407c1859
         * zan : 0
         * nickName : 炸鸡腿
         * dynid : HTDM0048abb3908b636e431c96589bcbc1901e61
         */

        @SerializedName("commentNum")
        public int commentNum;
        @SerializedName("content")
        public String content;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("icon")
        public String icon;
        @SerializedName("sex")
        public String sex;
        @SerializedName("createUser")
        public String createUser;
        @SerializedName("zan")
        public int zan;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("dynid")
        public String dynid;
        @SerializedName("pics")
        public List<PicsEntity> pics;


        public DynsEntity() {
        }

        protected DynsEntity(Parcel in) {
            this.commentNum = in.readInt();
            this.content = in.readString();
            this.createTime = in.readString();
            this.icon = in.readString();
            this.sex = in.readString();
            this.createUser = in.readString();
            this.zan = in.readInt();
            this.nickName = in.readString();
            this.dynid = in.readString();
            this.pics = in.createTypedArrayList(PicsEntity.CREATOR);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.commentNum);
            dest.writeString(this.content);
            dest.writeString(this.createTime);
            dest.writeString(this.icon);
            dest.writeString(this.sex);
            dest.writeString(this.createUser);
            dest.writeInt(this.zan);
            dest.writeString(this.nickName);
            dest.writeString(this.dynid);
            dest.writeTypedList(pics);
        }

        public static class PicsEntity implements Parcelable {
            public static final Creator<PicsEntity> CREATOR = new Creator<PicsEntity>() {
                public PicsEntity createFromParcel(Parcel source) {
                    return new PicsEntity(source);
                }

                public PicsEntity[] newArray(int size) {
                    return new PicsEntity[size];
                }
            };
            /**
             * url : http://ac-hy5srahi.clouddn.com/9AEsakFeTW4fJTiHnrH5JnD.jpeg
             */

            @SerializedName("url")
            public String url;

            public PicsEntity() {
            }

            protected PicsEntity(Parcel in) {
                this.url = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.url);
            }
        }
    }


}
