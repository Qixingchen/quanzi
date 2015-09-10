package com.tizi.quanzi.tool;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.tizi.quanzi.dataStatic.MyUserInfo;

/**
 * Created by qixingchen on 15/7/20.
 * 工具类
 */
public class Tool {

    /**
     * 检查是否存在SDCard
     *
     * @return SD是否可用
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取本设备应当显示的图像大小（0.6屏幕）
     *
     * @param context 上下文
     * @param Heigh   图片高
     * @param Weith   图片宽
     *
     * @return 计算得到的图片高，宽
     */
    public static int[] getImagePixel(Context context, int Heigh, int Weith) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        //宽度最大0.6
        int imageMaxWidth = dm.widthPixels * 3 / 5;
        int[] imagePixel = new int[2];
        if (Weith > imageMaxWidth) {
            imagePixel[1] = imageMaxWidth;
            Double imageHei = imageMaxWidth * 1.0 / Weith * Heigh;
            imagePixel[0] = imageHei.intValue();
        } else {
            imagePixel[0] = Heigh;
            imagePixel[1] = Weith;
        }
        return imagePixel;
    }

    /**
     * 判断当前用户是不是游客
     */
    public static boolean isGuest() {
        return MyUserInfo.getInstance().getUserInfo().getAccount().compareTo(StaticField.GuestUser.Account) == 0;
    }
}
