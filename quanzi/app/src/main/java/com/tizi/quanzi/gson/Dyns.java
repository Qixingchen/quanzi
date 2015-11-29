package com.tizi.quanzi.gson;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class Dyns extends OnlySuccess {
    /**
     * commentNum : 1
     * content : 巨人
     * createTime : 20151126202635
     * icon : https://dn-hy5srahi.qbox.me/o77Vt6BYzGhvpGhowjY0RQB.jpeg
     * zans : [{"icon":"https://dn-hy5srahi.qbox.me/o77Vt6BYzGhvpGhowjY0RQB.jpeg","userId":"HTDM004895dfca0c4c404097a8d3150df70b0d19"},{"icon":"http://ac-hy5srahi.clouddn.com/nTOn7NOihXiYVMVLHpwON3A.jpeg","userId":"HTDM00489e0c3c950a79424f960a3a7bcbd32f6a"}]
     * pics : [{"url":"https://dn-hy5srahi.qbox.me/kLhjrtPtaH9EOT66jKcLNpD.jpeg"},{"url":"https://dn-hy5srahi.qbox.me/dfeejUJYmW2HOTKxCjSBBeB.jpeg"},{"url":"https://dn-hy5srahi.qbox.me/GDtDiQHlpEzpvFYto7ZKCAE.jpeg"},{"url":"https://dn-hy5srahi.qbox.me/VbXadrR36Z8f6RLnn2hqPrD.jpeg"},{"url":"https://dn-hy5srahi.qbox.me/Xc4LBHSBl2mgiNe9Wo18P1B.jpeg"}]
     * sex : 1
     * createUser : HTDM004895dfca0c4c404097a8d3150df70b0d19
     * zan : 3
     * nickName : 11-11
     * dynid : HTDM0048381e497f38a64240a5aaffbeb91ab80a
     */

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
        public int commentNum;
        public String content;
        public String createTime;
        public String sex;
        public String icon;
        public String createUser;
        public int zan;
        public String nickName;
        public String dynid;
        public String groupName;
        public String senderId;
        public String activityId;
        public String groupIcon;
        public List<ZansEntity> zans;
        /**
         * icon : https://dn-hy5srahi.qbox.me/o77Vt6BYzGhvpGhowjY0RQB.jpeg
         * userId : HTDM004895dfca0c4c404097a8d3150df70b0d19
         */
        public List<PicsEntity> pics;

        public DynsEntity() {
        }


        protected DynsEntity(Parcel in) {
            this.commentNum = in.readInt();
            this.content = in.readString();
            this.createTime = in.readString();
            this.sex = in.readString();
            this.icon = in.readString();
            this.createUser = in.readString();
            this.zan = in.readInt();
            this.nickName = in.readString();
            this.dynid = in.readString();
            this.groupName = in.readString();
            this.senderId = in.readString();
            this.activityId = in.readString();
            this.groupIcon = in.readString();
            this.zans = in.createTypedArrayList(ZansEntity.CREATOR);
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
            dest.writeString(this.sex);
            dest.writeString(this.icon);
            dest.writeString(this.createUser);
            dest.writeInt(this.zan);
            dest.writeString(this.nickName);
            dest.writeString(this.dynid);
            dest.writeString(this.groupName);
            dest.writeString(this.senderId);
            dest.writeString(this.activityId);
            dest.writeString(this.groupIcon);
            dest.writeTypedList(zans);
            dest.writeTypedList(pics);
        }

        public static class ZansEntity implements Parcelable {
            public static final Parcelable.Creator<ZansEntity> CREATOR = new Parcelable.Creator<ZansEntity>() {
                public ZansEntity createFromParcel(Parcel source) {
                    return new ZansEntity(source);
                }

                public ZansEntity[] newArray(int size) {
                    return new ZansEntity[size];
                }
            };
            public String icon;
            public String userId;

            public ZansEntity(String icon, String userId) {
                this.icon = icon;
                this.userId = userId;
            }

            public ZansEntity() {
            }

            protected ZansEntity(Parcel in) {
                this.icon = in.readString();
                this.userId = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.icon);
                dest.writeString(this.userId);
            }
        }

        public static class PicsEntity implements Parcelable {
            public static final Parcelable.Creator<PicsEntity> CREATOR = new Parcelable.Creator<PicsEntity>() {
                public PicsEntity createFromParcel(Parcel source) {
                    return new PicsEntity(source);
                }

                public PicsEntity[] newArray(int size) {
                    return new PicsEntity[size];
                }
            };
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
