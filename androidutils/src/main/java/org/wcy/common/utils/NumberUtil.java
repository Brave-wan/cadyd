package org.wcy.common.utils;

/**
 * double的计算不精确，会有类似0.0000000000000002的误差，正确的方法是使用BigDecimal或者用整型
 * 整型地方法适合于货币精度已知的情况，比如12.11+1.10转成1211+110计算，最后再/100即可
 * 以下是摘抄的BigDecimal方法:
 */

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Number 数字处理类
 *
 * @author Atom
 */
public class NumberUtil implements Serializable {
    private static final long serialVersionUID = -3345205828566485102L;

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(Number value1, Number value2) {
        return add(Double.toString(value1.doubleValue()), Double.toString(value2.doubleValue()));
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }



    /**
     * getDouble
     *
     * @param value
     * @return
     */
    public static Double getDouble(Object value) {
        if (value == null) {
            return 0.00;
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b.doubleValue();
        } catch (Exception e) {
            return 0.00;
        }
    }

    /**
     * getInteger
     *
     * @param value
     * @return
     */
    public static Integer getInteger(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static float getFloat(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b.floatValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static BigDecimal getBigDecimal(Object value) {
        if (value == null) {
            return new BigDecimal(0);
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b;
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String getString(Object value, int scale) {
        StringBuffer sb = new StringBuffer();
        if (value != null && StringUtil.hasText(value.toString())) {

            if (value instanceof Double) {
                sb.append(value);
                int leng = sb.substring(sb.indexOf(".") + 1, sb.length()).length();
                if (leng > scale) {
                    sb.delete(sb.indexOf(".") + 3, sb.length());
                } else {
                    for (int i = 0; i < scale - leng; i++) {
                        sb.append("0");
                    }
                }
            } else if (value instanceof Integer) {
                sb.append(value);
                sb.append(".");
                for (int i = 0; i < scale; i++) {
                    sb.append("0");
                }
            } else if (value instanceof BigDecimal) {
                sb.append(((BigDecimal) value).doubleValue());
                int leng = sb.substring(sb.indexOf(".") + 1, sb.length()).length();
                if (leng > scale) {
                    sb.delete(sb.indexOf(".") + 3, sb.length());
                } else {
                    for (int i = 0; i < scale - leng; i++) {
                        sb.append("0");
                    }
                }
            } else {
                sb.append(value.toString());
                if(sb.indexOf(".")>-1){
                    int leng = sb.substring(sb.indexOf(".") + 1, sb.length()).length();
                    if (leng > scale) {
                        sb.delete(sb.indexOf(".") + 3, sb.length());
                    } else {
                        for (int i = 0; i < scale - leng; i++) {
                            sb.append("0");
                        }
                    }
                }else{
                    sb.append(".");
                    for (int i = 0; i <scale; i++) {
                        sb.append("0");
                    }
                }
            }
        } else {
            sb.append("0");
            if (scale > 0) {
                sb.append(".");
            }
            for (int i = 0; i < scale; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    /**
     * double 转int
     *
     * @param value
     * @return
     */
    public static int getInt(double value) {
        return new BigDecimal(value).intValue();
    }
}
