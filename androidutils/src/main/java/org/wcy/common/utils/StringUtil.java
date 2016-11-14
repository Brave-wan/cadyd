package org.wcy.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串验证辅助类
 *
 * @author wangchaoyong
 */
public class StringUtil {
    /**
     * 获取异常的详细信息
     *
     * @param e
     * @return
     */
    public static String getMessage(Exception e) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(buf, true));
        String expMessage = buf.toString();
        return expMessage;
    }

    public static long[] secToTime(long time) {
        long times[] = new long[4];
        long day = 0;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return null;
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
            }
            times[0] = day;
            times[1] = hour;
            times[2] = minute;
            times[3] = second;
        }
        return times;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }



    /**
     * 字符串是否为空
     *
     * @param str 字符串
     * @return true 为真 false 为假
     */
    private static boolean hasLength(CharSequence str) {
        return (str != null && !str.equals("") && !str.equals("null") && str
                .length() > 0);
    }


    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return true 为真不为null false 为null字符串包括空格
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str))
            return false;
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }



    public static Double getInt(String str) {
        if (hasText(str)) {
            return Double.parseDouble(str);
        } else {
            return 0.00;
        }
    }
}
