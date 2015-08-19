package com.tizi.quanzi.tool;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static String timeStringFromUNIX(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    public static int[] getImagePixel(Context context, int Heigh, int Weith) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        //宽度最大0.6
        int imageMaxWidth = dm.widthPixels * 3 / 5;
        int[] imagePixel = new int[2];
        if (Weith > imageMaxWidth) {
            imagePixel[1] = imageMaxWidth;
            Double imageHei = Weith * 1.0 / imageMaxWidth * Heigh;
            imagePixel[0] = imageHei.intValue();
        } else {
            imagePixel[0] = Heigh;
            imagePixel[1] = Weith;
        }


        return imagePixel;
    }

    private static int daysOfTwo(Date originalDate, Date compareDateDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(originalDate);
        int originalDay = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(compareDateDate);
        int compareDay = aCalendar.get(Calendar.DAY_OF_YEAR);

        return originalDay - compareDay;
    }

    public static String FriendlyDate(long timestamp) {
        Date nowDate = new Date();
        Date compareDate = new Date(timestamp);
        int dayDiff = daysOfTwo(nowDate, compareDate);
        String ans = "";

        if (dayDiff == 1) {
            ans += "昨日";
        } else if (dayDiff == 2) {
            ans += "前日";
        } else {
            ans += new SimpleDateFormat("M月d日", Locale.CHINESE).format(compareDate);
        }
        ans += new SimpleDateFormat(" HH:mm:ss", Locale.CHINESE).format(compareDate);
        return ans;
    }


}
