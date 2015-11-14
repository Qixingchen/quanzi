package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qixingchen on 15/11/14.
 */
public class BaiduLocation {

    /**
     * address : CN|福建|厦门|None|CHINANET|0|0
     * content : {"address_detail":{"province":"福建省","city":"厦门市","district":"","street":"","street_number":"","city_code":194},"address":"福建省厦门市","point":{"y":"2795265.28","x":"13147407.51"}}
     * status : 0
     */

    @SerializedName("address")
    public String address;
    /**
     * address_detail : {"province":"福建省","city":"厦门市","district":"","street":"","street_number":"","city_code":194}
     * address : 福建省厦门市
     * point : {"y":"2795265.28","x":"13147407.51"}
     */

    @SerializedName("content")
    public ContentEntity content;
    @SerializedName("status")
    public int status;

    public static class ContentEntity {
        /**
         * province : 福建省
         * city : 厦门市
         * district :
         * street :
         * street_number :
         * city_code : 194
         */

        @SerializedName("address_detail")
        public AddressDetailEntity addressDetail;
        @SerializedName("address")
        public String address;
        /**
         * y : 2795265.28
         * x : 13147407.51
         */

        @SerializedName("point")
        public PointEntity point;

        public static class AddressDetailEntity {
            @SerializedName("province")
            public String province;
            @SerializedName("city")
            public String city;
            @SerializedName("district")
            public String district;
            @SerializedName("street")
            public String street;
            @SerializedName("street_number")
            public String streetNumber;
            @SerializedName("city_code")
            public int cityCode;
        }

        public static class PointEntity {
            @SerializedName("y")
            public Double y;
            @SerializedName("x")
            public Double x;
        }
    }
}
