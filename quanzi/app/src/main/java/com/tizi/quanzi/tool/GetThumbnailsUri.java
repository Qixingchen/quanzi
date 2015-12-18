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
        if (!imageSourceUri.contains("webp")) {
            ans += "/format/webp";
        }
        return ans;
    }

    /**
     * 按照长宽获取,质量默认50
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

        return maxHeiAndWei(imageSourceUri, hei, wei, 50);
    }


    /**
     * 按照长宽获取
     *
     * @param imageSourceUri 原始uri
     * @param MaxHei         高最长 DP 长度
     * @param MaxWei         宽最长 DP 长度
     * @param quality        图片质量
     *
     * @return 处理后的uri
     */
    public static String maxDPHeiAndWei(String imageSourceUri, int MaxHei, int MaxWei, int quality, Context context) {
        int hei = getPXs(context, MaxHei);
        int wei = getPXs(context, MaxWei);

        return maxHeiAndWei(imageSourceUri, hei, wei, quality);
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
        String ans = imageSourceUri + addUri + "/1/w/" + MaxWei;

        ans += "/h/" + MaxHei + "/q/" + 75;
        if (!imageSourceUri.contains("webp")) {
            ans += "/format/webp";
        }
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
    public static String maxHeiAndWei(String imageSourceUri, int MaxHei, int MaxWei, int quality) {
        String ans = imageSourceUri + addUri + "/1/w/" + MaxWei;

        ans += "/h/" + MaxHei + "/q/" + quality;
        if (!imageSourceUri.contains("webp")) {
            ans += "/format/webp";
        }
        return ans;
    }

    /**
     * 更新网络状态获取图片链接
     * Wi-Fi 返回原图
     * 普通网络返回 半高,质量70的 webp 图
     *
     * @param imageSourceUri 图片地址
     * @param HeiDP          DP高度
     * @param WeiDP          DP宽度
     * @param context        上下文
     */
    public static String getUriLink(String imageSourceUri, int HeiDP, int WeiDP, Context context) {
        if (NetworkStatue.isWifi()) {
            return getWebPUri(imageSourceUri);
        } else {
            return maxDPHeiAndWei(imageSourceUri, HeiDP / 2, WeiDP / 2, 70, context);
        }
    }

    /**
     * 更新网络状态获取图片链接
     * Wi-Fi 返回原图
     * 普通网络返回 半高,质量70的 webp 图
     *
     * @param imageSourceUri 图片地址
     * @param HeiPX          PX高度
     * @param WeiPX          PX宽度
     */
    public static String getUriLink(String imageSourceUri, int HeiPX, int WeiPX) {
        if (NetworkStatue.isWifi()) {
            return getWebPUri(imageSourceUri);
        } else {
            return maxHeiAndWei(imageSourceUri, HeiPX / 2, WeiPX / 2, 70);
        }
    }

    public static String getWebPUri(String imageSourceUri) {
        if (!imageSourceUri.contains("webp") && !imageSourceUri.contains(addUri)) {
            return imageSourceUri + "?imageMogr/v2/format/webp";
        }
        return imageSourceUri;
    }

}
