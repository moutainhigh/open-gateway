package org.open.gateway.common.utils;

import java.time.*;

/**
 * Created by miko on 2019/11/12.
 * 日期工具类
 *
 * @author MIKO
 */
public class Dates {

    /**
     * 获取时区
     */
    private static ZoneId getZone() {
        return OffsetDateTime.now(ZoneId.systemDefault()).getOffset();
    }

    /**
     * 转时间戳
     *
     * @param date util日期
     */
    public static long toTimestamp(java.util.Date date) {
        return date.getTime();
    }

    /**
     * 转时间戳
     *
     * @param date sql日期
     */
    public static long toTimestamp(java.sql.Date date) {
        return date.getTime();
    }

    /**
     * 转时间戳
     *
     * @param localDate localDate日期
     */
    public static long toTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay(getZone()).toInstant().toEpochMilli();
    }

    /**
     * 转时间戳
     *
     * @param localDateTime localDateTime日期
     */
    public static long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(getZone()).toInstant().toEpochMilli();
    }

    /**
     * 转util date
     *
     * @param timestamp 时间戳
     */
    public static java.util.Date toUtilDate(long timestamp) {
        return new java.util.Date(timestamp);
    }

    /**
     * 转util date
     *
     * @param sqlDate sql日期
     */
    public static java.util.Date toUtilDate(java.sql.Date sqlDate) {
        return new java.util.Date(toTimestamp(sqlDate));
    }

    /**
     * 转util date
     *
     * @param localDate localDate日期
     */
    public static java.util.Date toUtilDate(LocalDate localDate) {
        return new java.util.Date(toTimestamp(localDate));
    }

    /**
     * 转util date
     *
     * @param localDateTime localDateTime日期
     */
    public static java.util.Date toUtilDate(LocalDateTime localDateTime) {
        return new java.util.Date(toTimestamp(localDateTime));
    }

    /**
     * 转sql date
     *
     * @param timestamp 时间戳
     */
    public static java.sql.Date toSqlDate(long timestamp) {
        return new java.sql.Date(timestamp);
    }

    /**
     * 转sql date
     *
     * @param utilDate util日期
     */
    public static java.sql.Date toSqlDate(java.util.Date utilDate) {
        return new java.sql.Date(toTimestamp(utilDate));
    }

    /**
     * 转sql date
     *
     * @param localDate localDate日期
     */
    public static java.sql.Date toSqlDate(LocalDate localDate) {
        return new java.sql.Date(toTimestamp(localDate));
    }

    /**
     * 转sql date
     *
     * @param localDateTime localDateTime日期
     */
    public static java.sql.Date toSqlDate(LocalDateTime localDateTime) {
        return new java.sql.Date(toTimestamp(localDateTime));
    }

    /**
     * 转local date
     *
     * @param timestamp 时间戳
     */
    public static LocalDate toLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(getZone()).toLocalDate();
    }

    /**
     * 转local date
     *
     * @param utilDate util日期
     */
    public static LocalDate toLocalDate(java.util.Date utilDate) {
        return toLocalDate(toTimestamp(utilDate));
    }

    /**
     * 转local date
     *
     * @param sqlDate sql日期
     */
    public static LocalDate toLocalDate(java.sql.Date sqlDate) {
        return sqlDate.toLocalDate();
    }

    /**
     * 转local date
     *
     * @param localDateTime localDateTime日期
     */
    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    /**
     * 转local date time
     *
     * @param timestamp 时间戳
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(getZone()).toLocalDateTime();
    }

    /**
     * 转local date time
     *
     * @param utilDate util日期
     */
    public static LocalDateTime toLocalDateTime(java.util.Date utilDate) {
        return utilDate.toInstant().atZone(getZone()).toLocalDateTime();
    }

    /**
     * 转local date time
     *
     * @param sqlDate sql日期
     */
    public static LocalDateTime toLocalDateTime(java.sql.Date sqlDate) {
        return sqlDate.toInstant().atZone(getZone()).toLocalDateTime();
    }

    /**
     * 转local date time
     *
     * @param localDate local date
     */
    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

}
