package open.gateway.common.utils;

import java.time.*;

/**
 * Created by miko on 2019/11/12.
 *
 * @author MIKO
 */
public class Dates {

    private static ZoneId getZone() {
        return OffsetDateTime.now(ZoneId.systemDefault()).getOffset();
    }

    public static Long toTimestamp(java.util.Date date) {
        return date.getTime();
    }

    public static Long toTimestamp(java.sql.Date date) {
        return date.getTime();
    }

    public static Long toTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay(getZone()).toInstant().toEpochMilli();
    }

    public static Long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(getZone()).toInstant().toEpochMilli();
    }

    public static java.util.Date toUtilDate(Long timestamp) {
        return new java.util.Date(timestamp);
    }

    public static java.util.Date toUtilDate(java.sql.Date sqlDate) {
        return new java.util.Date(toTimestamp(sqlDate));
    }

    public static java.util.Date toUtilDate(LocalDate localDate) {
        return new java.util.Date(toTimestamp(localDate));
    }

    public static java.util.Date toUtilDate(LocalDateTime localDateTime) {
        return new java.util.Date(toTimestamp(localDateTime));
    }


    public static java.sql.Date toSqlDate(Long timestamp) {
        return new java.sql.Date(timestamp);
    }

    public static java.sql.Date toSqlDate(java.util.Date utilDate) {
        return new java.sql.Date(toTimestamp(utilDate));
    }

    public static java.sql.Date toSqlDate(LocalDate localDate) {
        return new java.sql.Date(toTimestamp(localDate));
    }

    public static java.sql.Date toSqlDate(LocalDateTime localDateTime) {
        return new java.sql.Date(toTimestamp(localDateTime));
    }


    public static LocalDate toLocalDate(Long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(getZone()).toLocalDate();
    }

    public static LocalDate toLocalDate(java.util.Date utilDate) {
        return toLocalDate(toTimestamp(utilDate));
    }

    public static LocalDate toLocalDate(java.sql.Date sqlDate) {
        return sqlDate.toLocalDate();
    }

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }


    public static LocalDateTime toLocalDateTime(Long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(getZone()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(java.util.Date utilDate) {
        return utilDate.toInstant().atZone(getZone()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(java.sql.Date sqlDate) {
        return sqlDate.toInstant().atZone(getZone()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

}
