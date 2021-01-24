package com.highlands.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * 时间操作工具类
 *
 * @author xll
 * @date 2018/1/1
 */
public class DateUtil {
    private static final String TAG = DateUtil.class.getSimpleName();
    private static final String TIME_YM = "yyyyMM";
    private static final String TIME_YMD = "yyyyMMdd";
    private static final String TIME_YMD2 = "yyyy-MM-dd";
    private static final String TIME_YMDL = "yyyy/MM/dd";
    private static final String TIME_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_YMDHM2 = "yyyy年MM月dd日 HH:mm";
    private static final String TIME_YMDHM = "yyyy-MM-dd HH:mm";
    private static final String TIME_YMD3 = "MM月dd日";
    private static final String TIME_YMDHMS_WITHOUT_FORMAT = "yyyyMMddHHmmss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDddHHmmss", Locale.getDefault());

    public static String getCurrentTimeWithoutFormat() {
        simpleDateFormat.applyPattern(TIME_YMDHMS_WITHOUT_FORMAT);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTimeYM() {
        simpleDateFormat.applyPattern(TIME_YM);
        return simpleDateFormat.format(new Date());
    }

    public static String formatTimeYM(String time) {
        simpleDateFormat.applyPattern(TIME_YM);
        if (StringUtil.isStringNull(time) || time.length() < 6) {
            return "";
        }
        Date date;//解析字符串，转化为Date
        String t = time.substring(0, 6);
        try {
            date = simpleDateFormat.parse(t);
            return simpleDateFormat.format(date);//格式化String类型
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentTimeYMD() {
        simpleDateFormat.applyPattern(TIME_YMD2);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTimeYMDHMS() {
        simpleDateFormat.applyPattern(TIME_YMDHMS);
        return simpleDateFormat.format(new Date());
    }

    public static String formatYMD(Date date) {
        simpleDateFormat.applyPattern(TIME_YMD2);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间为 年月日 时分秒
     *
     * @param time
     * @return
     */
    public static String formatTimeYMDHMS(String time) {
        if (StringUtil.isStringNull(time) || time.length() < 14) {
            return time;
        }
        Date date;//解析字符串，转化为Date
        try {
            simpleDateFormat.applyPattern(TIME_YMDHMS_WITHOUT_FORMAT);
            date = simpleDateFormat.parse(time);
            simpleDateFormat.applyPattern(TIME_YMDHMS);
            return simpleDateFormat.format(date);//格式化String类型
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatYMDHMS(String time) {
        if (StringUtil.isStringNull(time) || time.length() < 19) {
            return time;
        }
        return time.substring(0, 19);
    }

    public static String formatYMDHM(String time) {
        if (StringUtil.isStringNull(time) || time.length() < 14) {
            return time;
        }
        Date date;//解析字符串，转化为Date
        try {
            simpleDateFormat.applyPattern(TIME_YMDHMS_WITHOUT_FORMAT);
            date = simpleDateFormat.parse(time);
            simpleDateFormat.applyPattern(TIME_YMDHM2);
            return simpleDateFormat.format(date);//格式化String类型
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取time与当前时间之间的年数
     */
    public static int getYearsBetweenNow(String time) {
        if (StringUtil.isStringNull(time)) {
            return -1;
        }
        simpleDateFormat.applyPattern(TIME_YMD);
        try {
            Date date = simpleDateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());//当前时间
            int nowYear = calendar.get(Calendar.YEAR);
            int nowDay = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(date); //传过来的时间
            int oldYear = calendar.get(Calendar.YEAR);
            int oldDay = calendar.get(Calendar.DAY_OF_YEAR);
            int years = nowYear - oldYear;
            if (nowDay - oldDay >= 0) {
                years = years + 1;
            }
            return years;
        } catch (ParseException e) {
            e.printStackTrace();
            Timber.tag(TAG).i(e);
        }
        return 0;
    }

    /**
     * 得到当前时间的几个月前或几个月后时间点
     *
     * @param months -1 1个月前  1 1个月后
     * @return eg：2017-2-23 8:37:50
     */
    public static String getTimeBeforeOrAfterMonths(int months) {
        simpleDateFormat.applyPattern(TIME_YMDHMS);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, months);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 得到当前时间的几天前或几天后时间点
     *
     * @param days
     * @return
     */
    public static String getTimeBeforeOrAfterDays(int days) {
        simpleDateFormat.applyPattern(TIME_YMDHMS);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Make the date format yyyy/MM/dd
     * 将时间格式化为 年/月/日
     *
     * @param str the string to be formatted
     *            被格式化的字符串
     * @return yyyy/MM/dd
     */
    public static String dateFormatYMDL(String str) {
        if (StringUtil.isStringNull(str)) {
            return "";
        }
        String time = str;
        Date date;//解析字符串，转化为Date
        if (str.length() == 8) {
            simpleDateFormat.applyPattern(TIME_YMD);
        } else if (str.length() >= 10) {
            time = str.substring(0, 10);
            simpleDateFormat.applyPattern(TIME_YMD2);
        }
        try {
            date = simpleDateFormat.parse(time);
            simpleDateFormat.applyPattern(TIME_YMDL);
            return simpleDateFormat.format(date);//格式化String类型
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String dateFormatYMD3(String str) {
        if (StringUtil.isStringNull(str)) {
            return "";
        }
        String time = str;
        Date date;//解析字符串，转化为Date
        if (str.length() == 8) {
            simpleDateFormat.applyPattern(TIME_YMD);
        } else if (str.length() >= 10) {
            time = str.substring(0, 10);
            simpleDateFormat.applyPattern(TIME_YMD2);
        }
        try {
            date = simpleDateFormat.parse(time);
            simpleDateFormat.applyPattern(TIME_YMD3);
            return simpleDateFormat.format(date);//格式化String类型
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param month 获得当前日期的前几个月的日期
     * @return yyyyMMdd
     */
    public static String getDateBeforeOrAfterMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        simpleDateFormat.applyPattern(TIME_YMD);
        return simpleDateFormat.format(date);
    }

    /**
     * @param day 获得当前日期的前几天或几天后
     * @return yyyyMMdd
     */
    public static String getDateBeforeOrAfterDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, day);
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        simpleDateFormat.applyPattern(TIME_YMD);
        return simpleDateFormat.format(date);
    }

    public static String formatYMD2(String time) {
        simpleDateFormat.applyPattern(TIME_YMD2);
        try {
            Date date = simpleDateFormat.parse(time);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }


    //判断选择的日期是否是今天
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    public static boolean isToday(String time) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_YMD2);
        Date d1 = null;
        Date nowDate = new Date();
        Date d2 = null;
        long day = -1;
        try {
            d1 = format.parse(time);
            d2 = format.parse(format.format(nowDate));
            day = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (day == 0) {
            return true;
        } else {
            return false;
        }

    }

    //判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }


    //判断选择的日期是否是本月
    public static boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }


    /**
     * 获取今天所在周的第一天
     *
     * @return
     * @throws ParseException
     */
    public static String getFirstOfWeek() {
        Calendar cal = Calendar.getInstance();
        simpleDateFormat.applyPattern(TIME_YMD2);
        try {
            cal.setTime(simpleDateFormat.parse(getCurrentTimeYMD()));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        String data1 = simpleDateFormat.format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        // 所在周结束日期
        String data2 = simpleDateFormat.format(cal.getTime());
        return data1;

    }

    public static String getAfterDay(String time, int days) {
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        simpleDateFormat.applyPattern(TIME_YMD2);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, days);
        Date date1 = calendar.getTime();
        return simpleDateFormat.format(date1);
    }

    /**
     * 根据日期 找到对应日期的 星期
     *     
     */
    public static String getDayOfWeekByDate(String date) {
        String dayOfweek = "-1";
        simpleDateFormat.applyPattern(TIME_YMD2);
        try {
            Date myDate = simpleDateFormat.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return dayOfweek.replace("周", "星期");
    }

    /**
     * 比较时间
     *
     * @param beginTime
     * @param endTime
     * @return 开始时间比结束时间小，返回true
     */
    public static boolean compareDate(String beginTime, String endTime) {
        if (StringUtil.isStringNull(beginTime) || StringUtil.isStringNull(endTime)) {
            return false;
        }
        simpleDateFormat.applyPattern(TIME_YMD2);
        try {
            Date beginDate = simpleDateFormat.parse(beginTime);
            Date endDate = simpleDateFormat.parse(endTime);
            Timber.tag(TAG).d(beginTime + " " + endTime + " " + beginDate.before(endDate));
            return beginDate.before(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 格式化为 yyyy-MM-dd HH:mm
     *
     * @param milliSecond 时间戳
     * @return 时间
     */
    public static String getTimeYmdhm(long milliSecond) {
        Date date = new Date();
        date.setTime(milliSecond);
        simpleDateFormat.applyPattern(TIME_YMDHM);

        return simpleDateFormat.format(utcToBeiJing(date));
    }

    /**
     * utc时间转为北京时间
     *
     * @param date utc时间
     * @return date
     */
    public static Date utcToBeiJing(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return calendar.getTime();
    }
}
