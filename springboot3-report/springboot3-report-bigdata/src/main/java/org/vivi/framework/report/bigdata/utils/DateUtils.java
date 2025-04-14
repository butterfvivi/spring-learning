package org.vivi.framework.report.bigdata.utils;

import cn.hutool.core.date.DateTime;

import java.time.format.DateTimeFormatter;

/**
 * 功能点:
 * 基本功能:获取当前时间
 * 一.日期的格式转换
 * 1.日期对象转字符串
 * 2.日期对象转时间戳
 * 3.时间戳转日期对象
 * 4.时间戳转字符串
 * 二.日期的加减
 * 1.当前日期加减N天
 * 2.当前日期加减N周
 * 3.当前日期加减N月
 * 4.当前日期加减N年
 * 三.日期坐标转换
 * 1.指定日期是本周的周几
 * 2.指定日期是本年的第几周
 * 四.日期单位的特殊点
 * 1.根据日期,获取当前的起止时间戳
 * 2.根据日期,获取本周的第一天的0点时间戳
 * 3.根据日期,获取本周的最后一天的23:59:59的时间戳
 * 4.根据日期,获取本月的第一天的0点时间戳
 * 5.根据日期,获取本月的最后一天的23:59:59的时间戳
 * 6.根据日期,获取本年的第一天的0点时间戳
 * 7.根据日期,获取本年的最后一天的23:59:59的时间戳
 * 五.日期大小的比较
 * 1.两个日期,比较返回大小
 * 2.两个日期,比较返回差额,判断是否满足某一个阈值
 * 六.日期截断
 * 1.获取指定日期的天的毫秒数
 * 2.获取指定日期的小时的毫秒数
 * 3.获取指定日期的分的毫秒数
 * 4.获取指定日期的秒的毫秒数
 * 七.获取时间段
 * 1.获取起止日期之间的所有日期列表
 * 2.获取起止日期之间的所有周列表
 * 3.获取起止日期之间的所有月列表
 */
public class DateUtils {

    /**
     * 日期格式枚举类
     */
    public enum Parttern {

        /**中杠分隔符**/
        FORMAT_YYMM_MID("yyyy-MM"),
        FORMAT_YYWW_MID("yyyy-ww"),
        FORMAT_YYMMDD_MID("yyyy-MM-dd"),
        FORMAT_YYMMDDH_MID("yyyy-MM-dd HH"),
        FORMAT_YYMMDDHM_MID("yyyy-MM-dd HH:mm"),
        FORMAT_YYMMDDHMS_MID("yyyy-MM-dd HH:mm:ss"),
        FORMAT_YYMMDDHMSS_MID("yyyy-MM-dd HH:mm:ss SSS"),
        /**无隔符**/
        FORMAT_YYMM_NON("yyyyMM"),
        FORMAT_YYWW_NON("yyyyww"),
        FORMAT_YYMMDD_NON("yyyyMMdd"),
        FORMAT_YYMMDDH_NON("yyyyMMddHH"),
        FORMAT_YYMMDDHM_NON("yyyyMMddHHmm"),
        FORMAT_YYMMDDHMS_NON("yyyyMMddHHmmss"),
        FORMAT_YYMMDDHMSS_NON("yyyyMMddHHmmssSSS"),

        /**斜杠分隔符**/
        FORMAT_YYMM_SLASH("yy/yy/MM"),
        FORMAT_YYWW_SLASH("yyyy/ww"),
        FORMAT_YYMMDD_SLASH("yyyy/MM/dd"),
        FORMAT_YYMMDDH_SLASH("yyyy/MM/dd HH"),
        FORMAT_YYMMDDHM_SLASH("yyyy/MM/dd HH:mm"),
        FORMAT_YYMMDDHMS_SLASH("yyyy/MM/dd HH:mm:ss"),
        FORMAT_YYMMDDHMSS_SLASH("yyyy/MM/dd HH:mm:ss:SSS"),

        ;

        private String fmt;

        Parttern(String fmt) {
            this.fmt = fmt;
        }

        public String getFmt() {
            return fmt;
        }
    }

    /**新建时间对象**/
    public static DateTime create(){
        return DateTime.now();
    }

    /**注意:timestamp为13位,否则新建出来的日期是1970年1月18号**/
    public static DateTime create(long timestamp){
        return new DateTime(timestamp);
    }

    public static boolean between(DateTime src,DateTime left, DateTime right){
        if( src.isAfter(left) && src.isBefore(right) ){
            return true;
        }
        return false;
    }

}
