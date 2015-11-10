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
        if (timestamp == 0) {
            return "";
        }
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

    /**
     * 从UNIX 转化为 时间
     *
     * @param timestamp UNIXTime(ms)
     *
     * @return HH mm ss
     */
    private static int[] getDayTimeFromUNIX(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE);
        String date = sdf.format(new Date(timestamp));
        String time[] = date.split(":");
        int[] ans = new int[3];
        for (int i = 0; i < 3; i++) {
            ans[i] = Integer.valueOf(time[i]);
        }
        return ans;
    }

    /**
     * 从 HH:mm:ss 转化为 时间
     *
     * @param time HH:mm:ss
     *
     * @return HH mm ss
     */
    private static int[] getDayTimeFromString(String time) {
        String times[] = time.split(":");
        int[] ans = new int[3];
        for (int i = 0; i < 3; i++) {
            ans[i] = Integer.valueOf(times[i]);
        }
        return ans;
    }


    /**
     * 从 HH mm ss 得到是今天的第几秒
     *
     * @param time 以上两个函数的结果
     *
     * @return 第几秒
     */
    private static int getDayS(int[] time) {
        return time[0] * 3600 + time[1] * 60 + time[2];
    }

    /**
     * 判断是否在活动时间内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     *
     * @return 是否在时间内
     */
    public static boolean isInThemeTime(String startTime, String endTime) {
        int now = getDayS(getDayTimeFromUNIX(Tool.getBeijinTime()));
        int start = getDayS(getDayTimeFromString(startTime));
        int end = getDayS(getDayTimeFromString(endTime));

        //如果是跨天的活动
        if (start > end) {
            if (now >= start || now < end) {
                return true;
            } else {
                return false;
            }
        }

        //并不是跨天的
        if (now < start || now > end) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取活动结束倒计时 (s)
     */
    public static int getThemeCountDown(String startTime, String endTime) {
        if (!isInThemeTime(startTime, endTime)) {
            return 0;
        }
        final int AllDayS = 24 * 3600;
        int now = getDayS(getDayTimeFromUNIX(Tool.getBeijinTime()));
        int start = getDayS(getDayTimeFromString(startTime));
        int end = getDayS(getDayTimeFromString(endTime));
        //如果是跨天的活动
        if (start > end) {
            if (now > start) {
                return end + AllDayS - now;
            } else {
                return end - now;
            }
        }
        //并不是跨天的
        return end - now;
    }

    public static int getAge(String birthday) {
        String[] bir = birthday.split("-");
        return getNowYear() - Integer.valueOf(bir[0]);
    }

    public static String getXingzuo(String birthday) {
        String[] bir = birthday.split("-");
        int m = Integer.valueOf(bir[1]);
        int d = Integer.valueOf(bir[2]);
        String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
        Integer[] arr = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
        Integer num = m * 2 - (d < arr[m - 1] ? 2 : 0);
        return s.substring(num, num + 2);

    }

    private static int getNowYear() {
        String date = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date());
        return Integer.valueOf(date);
    }
}
