package com.zarddy.library.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日期时间
 */

public class DateTimeUtils {

    /**
     * 时间戳转换成日期
     * @param millis 时间戳
     * @param justMonthDay 如果为 true，则转换成 月-日
     */
    public static String convertMillisToDate(long millis, boolean justMonthDay) {
        try {
            String format = justMonthDay ? "MM-dd" : "yyyy-MM-dd"; // 是否只显示 月日
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(millis);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 时间戳转换成时间
     * @param millis java格式时间戳
     * @return 如：2017-09-04 20:18
     */
    public static String convertMillisToFullTime(long millis) {
        try {
            String format = "yyyy-MM-dd HH:mm:ss"; // 年月日 时分
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(millis);
        } catch (Exception e) {
            return "";
        }
    }
}
