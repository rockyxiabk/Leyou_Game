package com.leyou.game.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Description : 时间格式转化类
 *
 * @author : rocky
 * @Create Time : 2017/4/27 下午2:20
 * @Modified By: rocky
 * @Modified Time : 2017/4/27 下午2:20
 */
public class DataUtil {
    public static SimpleDateFormat Y_M_D_HM = new SimpleDateFormat("yyyy-MM-dd HH:mm");//2016-05-02 13:02
    public static SimpleDateFormat M_D_HM = new SimpleDateFormat("MM-dd HH:mm");//05-02 13:02
    public static SimpleDateFormat Y_M_D_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2016-05-02 13:02:53
    public static SimpleDateFormat Md_HM = new SimpleDateFormat("MM月dd日  HH:mm");//05月02日 13:02
    public static SimpleDateFormat YMdHms = new SimpleDateFormat("yyyyMMddHHmmss");//20160502120253
    public static SimpleDateFormat YMD = new SimpleDateFormat("yyyyMMdd");//20160502
    public static SimpleDateFormat Y_M_D = new SimpleDateFormat("yyyy-MM-dd");//2016-05-02
    public static SimpleDateFormat Y = new SimpleDateFormat("yyyy");//2016-**-**
    public static SimpleDateFormat M = new SimpleDateFormat("MM");//****-05-**
    public static SimpleDateFormat D = new SimpleDateFormat("dd");//****-**-02
    public static SimpleDateFormat HMS = new SimpleDateFormat("HH:mm:ss");//13:02:55
    public static SimpleDateFormat HM = new SimpleDateFormat("HH:mm");//12:02
    public static SimpleDateFormat MS = new SimpleDateFormat("mm:ss");//36:56
    private static final String TAG = "DataUtil";

    /**
     * long型字符串转化为date
     *
     * @param date
     * @return
     */
    public static Date sdfFormat(String date) {
        Date date1 = new Date(date);
        return date1;
    }

    public static String getConvertResult(long timeStamp, SimpleDateFormat simpleDateFormat) {
        String format = null;
        format = simpleDateFormat.format(new Date(timeStamp));
        return format;
    }

    public static int getResultByTimeStamp(long timeStamp, SimpleDateFormat simpleDateFormat) {
        int result;
        String format = null;
        format = simpleDateFormat.format(new Date(timeStamp));
        result = Integer.valueOf(format);
        return result;
    }

    public static long getTimeStampBuyTime(int year, int month, int day) {
        String re_time;
        long timeStamp = 0l;
        String user_time = year + "年" + month + "月" + day + "日";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        Date d = new Date();
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            timeStamp = Long.valueOf(str);
            return timeStamp;
        } catch (ParseException e) {
            return timeStamp;
        }
    }

    /**
     * date转换为long型时间戳
     *
     * @param time
     * @param simpleDateFormat
     * @return
     */
    public static long getTimeStampByString(String time, SimpleDateFormat simpleDateFormat) {
        Date date = null;
        long longTime;
        try {
            date = simpleDateFormat.parse(time);
            longTime = date.getTime();
            return longTime;
        } catch (ParseException e) {
            LogUtil.e(TAG, e.toString());
            return 0;
        }
    }

    public static long getdaytime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt2.getTime();
    }

    public static int percent(int total, int per) {
        if (total == 0) {
            return 0;
        } else {
            return per * 100 / total;
        }
    }

    /**
     * 计算百分比
     *
     * @param total
     * @param per
     * @return
     */
    public static String percentStr(int total, int per) {
        DecimalFormat df = new DecimalFormat("##0.##");
        return df.format(1.0 * per * 100 / total);
    }

    /**
     * 通过时间戳计算年龄
     *
     * @param timeStamp
     * @return
     */
    public static String getAgeBuyTimeStamp(long timeStamp) {
        String format = null;
        int year1;
        int year2;
        format = Y.format(new Date(timeStamp));
        year1 = Integer.valueOf(format);
        format = Y.format(new Date());
        year2 = Integer.valueOf(format);
        int age = year2 - year1;
        return age + "";
    }

    /**
     * 获取对应的星座
     *
     * @return 对应的星座
     */
    public static String getConstellation(long timeStamp) {
        String format = null;
        int month = 1;
        int day = 1;
        format = M.format(new Date(timeStamp));
        month = Integer.valueOf(format);
        format = D.format(new Date(timeStamp));
        day = Integer.valueOf(format);
        LogUtil.d(TAG, "月份：" + month + "----日期：" + day);
        String[] constellation = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
        int[] arr = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};// 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座
        return constellation[index];
    }

    /**
     * 判断相隔时间
     *
     * @param lastTime
     * @param currentTime
     * @return
     */
    public static String msg_distance_time(long lastTime, long currentTime) {
        String timeStamp = DataUtil.sdfyyyyMMdd(new Date(lastTime));
        String current = DataUtil.sdfyyyyMMdd(new Date(currentTime));

        int distanceDays = distance_days(timeStamp, current);
        String hms = DataUtil.getConvertResult(lastTime, DataUtil.HM);
        if (distanceDays < 1) {
            int distance_millis = distance_millis(lastTime, currentTime);
            if (distance_millis == 0) {
                return "刚刚";
            } else {
                return hms;
            }
        } else if (1 == distanceDays) {
            return "昨天 ";
        } else if (1 < distanceDays && distanceDays <= 15) {
            return distanceDays + "天前";
        } else if (15 < distanceDays && distanceDays <= 30) {
            return distanceDays + "天前";
        } else if (distanceDays > 30) {
            int count = distanceDays / 30;
            return count + "个月前";
        } else {
            return "";
        }
    }

    /**
     * 判断相隔天数
     *
     * @param lastTime
     * @param currentTime
     * @return
     */
    public static int distanceDay(long lastTime, long currentTime) {
        String timeStamp = DataUtil.sdfyyyyMMdd(new Date(lastTime));
        String current = DataUtil.sdfyyyyMMdd(new Date(currentTime));
        return distance_days(timeStamp, current);
    }

    /**
     * 获得两个时间段的相隔天数
     *
     * @param lastTime
     * @param currentTime
     * @return
     */
    public static int distance_days(String lastTime, String currentTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(lastTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);//从间隔毫秒变成间隔天数
        return (int) dayCount;
    }

    public static int distance_millis(long lastTime, long currentTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(getsdfyyyyMMddHHmmss(lastTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(getsdfyyyyMMddHHmmss(currentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        long distance_millis = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        if (distance_millis < 1000 * 60 * 60) {//1小时内
            return 0;
        } else {//大于1小时
            return 1;
        }
    }

    /**
     * 获取相应格式时间
     * 20160502120253
     *
     * @param current
     * @return
     */
    public static String getsdfyyyyMMddHHmmss(long current) {
        String format = null;
        format = YMdHms.format(new Date(current));
        return format;
    }

    public static String sdfyyyyMMdd(Date date) {
        return YMD.format(date);
    }
}
