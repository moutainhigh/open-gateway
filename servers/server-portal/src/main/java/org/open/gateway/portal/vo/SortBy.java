package org.open.gateway.portal.vo;

import lombok.Getter;

import java.util.*;

/**
 * Created by miko on 2019/12/2.
 *
 * @author MIKO
 */
public class SortBy {

    /**
     * 前端排序名称
     */
    @Getter
    private final String key;

    /**
     * 数据库字段名称
     */
    private final String fieldName;

    public SortBy(String key, String fieldName) {
        if (key == null) {
            throw new IllegalArgumentException("Sort by key can not be null");
        }
        this.key = key;
        this.fieldName = fieldName;
    }

    /**
     * 获取排序字段
     *
     * @return 排序字段
     */
    public String getSortBy() {
        return Optional.ofNullable(fieldName).orElse(key);
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortBy sortBy = (SortBy) o;
        // 这里只根据key判断重复
        return Objects.equals(key, sortBy.key);
    }

    @Override
    public int hashCode() {
        // 这里只根据key生成hash
        return Objects.hash(key);
    }

    public static SortBy of(String key, String fieldName) {
        return new SortBy(key, fieldName);
    }

    public static SortBy of(String key) {
        return new SortBy(key, null);
    }

    public static Set<SortBy> sets(SortBy... sortBys) {
        return new HashSet<>(Arrays.asList(sortBys));
    }

    @Getter
    public enum SortType {

        ASC,
        DESC;

        public static SortType from(String sortType) {
            return SortType.valueOf(sortType.toUpperCase());
        }

    }

}
