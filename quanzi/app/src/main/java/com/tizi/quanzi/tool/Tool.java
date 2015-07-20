package com.tizi.quanzi.tool;

import android.os.Environment;

/**
 * Created by qixingchen on 15/7/20.
 * 检查是否存在SDCard
 */
public class Tool {

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
