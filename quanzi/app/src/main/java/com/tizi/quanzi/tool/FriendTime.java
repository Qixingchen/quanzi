package com.tizi.quanzi.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by qixingchen on 15/8/19.
 */
public class FriendTime {

    public static String timeStringFromUNIX(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        String date = sdf.format(new Date(timestamp));
        return date;
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
