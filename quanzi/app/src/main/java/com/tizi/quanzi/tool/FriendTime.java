package com.tizi.quanzi.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by qixingchen on 15/8/19.
 * 获取时间的友好表达
 */
public class FriendTime {

    /**
     * 从UNIX 转化为 yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp UNIXTime(ms)
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String timeStringFromUNIX(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    /**
     * 获取两个时间的差值（天）
     * todo 大于一年？跨年？
     *
     * @param originalDate    被减数
     * @param compareDateDate 减数
     *
     * @return 天数差异
     */
    private static int getDiffOfTwoDays(Date originalDate, Date compareDateDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(originalDate);
        int originalDay = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(compareDateDate);
        int compareDay = aCalendar.get(Calendar.DAY_OF_YEAR);

        return originalDay - compareDay;
    }


    /**
     * 从UNIX 转化为 M月d日 HH:mm:ss （“”、昨日、前日）
     *
     * @param timestamp UNIXTime(ms)
     *
     * @return M月d日 HH:mm:ss （“”、昨日、前日）
     */
    public static String FriendlyDate(long timestamp) {
        Date nowDate = new Date();
        Date compareDate = new Date(timestamp);
        int dayDiff = getDiffOfTwoDays(nowDate, compareDate);
        String ans = "";

        if (dayDiff == 0) {

        } else if (dayDiff == 1) {
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
