package com.tizi.quanzi.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        return sdf.format(new Date(timestamp));
    }

    /**
     * 从服务器时间 ( yyyyMMddHHmmss ) 转化为 UNIX time
     *
     * @param time 服务器时间 ( yyyyMMddHHmmss )
     *
     * @return UNIX time or 0 if format not match
     */
    public static long getTimeFromServerString(String time) {
        SimpleDateFormat fromServer = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        try {
            return fromServer.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getServerTime() {
        SimpleDateFormat fromServer = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        return fromServer.format(new Date());
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
        int sForDay = 24 * 3600 * 1000;
        int day1 = (int) (originalDate.getTime() / sForDay);
        int day2 = (int) (compareDateDate.getTime() / sForDay);

        return day1 - day2;
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

    /**
     * 获取用户年龄
     */
    public static int getAge(String birthday) {
        String[] bir = birthday.split("-");
        return getNowYear() - Integer.valueOf(bir[0]);
    }

    /**
     * 获取用户星座
     */
    public static String getXingzuo(String birthday) {
        String[] bir = birthday.split("-");
        int m = Integer.valueOf(bir[1]);
        int d = Integer.valueOf(bir[2]);
        String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
        Integer[] arr = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
        Integer num = m * 2 - (d < arr[m - 1] ? 2 : 0);
        return s.substring(num, num + 2);

    }

    /**
     * 获取当前年份
     */
    private static int getNowYear() {
        String date = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date());
        return Integer.valueOf(date);
    }
}
