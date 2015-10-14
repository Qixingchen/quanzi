package com.tizi.quanzi.tool;

import android.content.Context;

/**
 * Created by qixingchen on 15/8/18.
 * 获取7牛的缩略图
 */
public class GetThumbnailsUri {

    private static final String addUri = "?imageView2";

    /**
     * 获取DPI
     */
    public static float getDpi(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取像素大小
     *
     * @param dp 所需的图像DP大小
     */
    public static int getPXs(Context context, int dp) {
        return ((int) (dp * getDpi(context)));
    }

    /**
     * 按照最长获取
     *
     * @param imageSourceUri 原始uri
     * @param MaxLong        长边最长长度
     * @param MaxShort       短边最长长度
     *
     * @return 处理后的uri
     */
    public static String maxLongSide(String imageSourceUri, int MaxLong, int MaxShort) {
        String ans = imageSourceUri + addUri + "/0/w/" + MaxLong;
        if (MaxShort != 0) {
            ans += "/h/" + MaxShort;
        }
        return ans;
    }

    /**
     * 按照长宽获取
     *
     * @param imageSourceUri 原始uri
     * @param MaxHei         高最长 DP 长度
     * @param MaxWei         宽最长 DP 长度
     *
     * @return 处理后的uri
     */
    public static String maxDPHeiAndWei(String imageSourceUri, int MaxHei, int MaxWei, Context context) {
        int hei = getPXs(context, MaxHei);
        int wei = getPXs(context, MaxWei);
        String ans = imageSourceUri + addUri + "/2/w/" + wei;

        ans += "/h/" + hei;

        return ans;
    }

    /**
     * 按照长宽获取
     *
     * @param imageSourceUri 原始uri
     * @param MaxHei         高最长长度
     * @param MaxWei         宽最长长度
     *
     * @return 处理后的uri
     */
    public static String maxHeiAndWei(String imageSourceUri, int MaxHei, int MaxWei) {
        String ans = imageSourceUri + addUri + "/2/w/" + MaxWei;

        ans += "/h/" + MaxHei;

        return ans;
    }

}
