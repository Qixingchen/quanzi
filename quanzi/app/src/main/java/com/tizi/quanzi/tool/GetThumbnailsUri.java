package com.tizi.quanzi.tool;

/**
 * Created by qixingchen on 15/8/18.
 */
public class GetThumbnailsUri {

    private static final String addUri = "?imageView2";

    public static String maxLongSide(String imageSourceUri, int MaxLong, int MaxShort) {
        String ans = imageSourceUri + addUri + "/0/w/" + MaxLong;
        if (MaxShort != 0) {
            ans += "/h/" + MaxShort;
        }
        return ans;
    }

    public static String maxHeiAndWei(String imageSourceUri, int MaxHei, int MaxWei) {
        String ans = imageSourceUri + addUri + "/2/w/" + MaxWei;

        ans += "/h/" + MaxHei;

        return ans;
    }

}
