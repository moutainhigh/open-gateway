package org.open.gateway.test.sql;

/**
 * Created by miko on 10/13/20.
 *
 * @author MIKO
 */
public interface SqlParser {

    /**
     * 解析sql
     *
     * @param sql 原始sql
     * @return 解析后的sql
     */
    String parse(String sql, Object... args);

}
