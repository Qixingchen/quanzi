package com.tizi.quanzi.tool;

/**
 * Created by qixingchen on 15/8/18.
 * 获取7牛的缩略图
 */
public class GetThumbnailsUri {

    private static final String addUri = "?imageView2";

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
