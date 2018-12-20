package com.util;

import com.utils.lib.ss.common.DateHepler;

/**
 * Created by E on 2017/12/28.
 */
public class DateUtil {

    /**
     * 将秒转化为小时显示。
     * @param seconds 总秒数
     */
    public static String secondsFormatHours(int seconds){
        if (seconds < 0){
            return "00";
        }
        if (seconds < 60){
            return formatDigs(seconds);
        }else if (seconds >= 60 && seconds < 3600){
            int minutes = seconds/60;
            int secondsL = seconds%60;
            return formatDigs(minutes) + ":" + formatDigs(secondsL);
        }else {
            int minutes = seconds/60;
            int hours = minutes/60;
            int minutesL = minutes%60;
            int secondsL = seconds%60;
            return formatDigs(hours) + ":" + formatDigs(minutesL) + ":" + formatDigs(secondsL);
        }
    }

    /**
     * 将秒转化为小时显示。
     * @param seconds 总秒数
     */
    public static String secondsFormatHours1(int seconds){
        if (seconds < 0){
            return "00";
        }
        if (seconds < 60){
            return "00:" + formatDigs(seconds);
        }else if (seconds >= 60 && seconds < 3600){
            int minutes = seconds/60;
            int secondsL = seconds%60;
            return formatDigs(minutes) + ":" + formatDigs(secondsL);
        }else {
            int minutes = seconds/60;
            int hours = minutes/60;
            int minutesL = minutes%60;
            int secondsL = seconds%60;
            return formatDigs(hours) + ":" + formatDigs(minutesL) + ":" + formatDigs(secondsL);
        }
    }

    private static String formatDigs(int digs){
        return digs < 10 ? ("0" + digs) : String.valueOf(digs);
    }

    /**
     * （今天之内的日期：显示几点几分，如：12:03；昨天的日期显示昨天；昨天之前显示日月，如：12-03；今年之前显示年月日，如：2017-11-23）
     * @param timestamp
     * @return
     */
    public static String fomatSpecialTime(long timestamp){
        long nowTime = System.currentTimeMillis()/1000;
        int year = DateHepler.getYear();
        int monthOfYear = DateHepler.getMonthOfYear();
        int dayOfYear = DateHepler.getDayOfYear(nowTime);

        int newYear = DateHepler.getYear(timestamp);
        int newMonthOfYear = DateHepler.getMonthOfYear(timestamp);
        int newDayOfYear = DateHepler.getDayOfYear(timestamp);

        if (year != newYear){
            return DateHepler.timestampFormat(timestamp , "yyyy-MM-dd");
        }
        if (monthOfYear != newMonthOfYear){
            return DateHepler.timestampFormat(timestamp , "MM-dd");
        }
        if (dayOfYear == newDayOfYear){
            return DateHepler.timestampFormat(timestamp , "HH:mm");
        }
        if ((dayOfYear - newDayOfYear) == 1){
            return "昨天";
        }
        if (newDayOfYear > dayOfYear){
            return DateHepler.timestampFormat(timestamp , "yyyy-MM-dd");
        }
        return DateHepler.timestampFormat(timestamp , "MM-dd");
    }

    /**
     * 配速转换。
     * @param secondsPerKm 一KM需要多少秒
     * @return 相应格式的字符串
     */
    public static String seconds2RunningPace(int secondsPerKm){
        int totalSeconds = secondsPerKm;
        int minutes = 0;
        int second = 0;
        minutes = totalSeconds/60;
        second = totalSeconds%60;

        String text = "--";
        if (0 == minutes) {
            if (0 != second) {
                if (second < 10) {
                    text = "0"+ second + "\"";
                }else {
                    text = second + "\"";
                }
            }
        }else {
            String minTxt = null;
            if (minutes < 10) {
                minTxt = "0"+ minutes + "\'" ;
            }else {
                minTxt = minutes + "\'" ;
            }

            if (0 == second) {
                text = minTxt + "00" + "\"";
            }else {
                if (second < 10) {
                    text = minTxt + "0"+second + "\"";
                }else {
                    text = minTxt + second + "\"";
                }
            }
        }
        return text;
    }

}
