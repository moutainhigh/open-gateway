package org.open.gateway.route.utils.sql;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by miko on 2020/8/3.
 *
 * @author MIKO
 */
public class Sql {

    private final String sql;

    public Sql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return this.sql;
    }

    public String format(Object... args) {
        if (args == null || args.length == 0) {
            return this.toString();
        }
        return String.format(this.toString(), args);
    }

    public Sql whereIn(String column, Collection<?> args) {
        return in(" WHERE ", column, args);
    }

    public Sql andIn(String column, Collection<?> args) {
        return in(" AND ", column, args);
    }

    public Sql in(String prefix, String column, Collection<?> args) {
        if (args == null) {
            return new Sql(this.toString());
        }
        return new Sql(this.toString() + prefix + column + getWhereInString(args));
    }

    private String getWhereInString(Collection<?> args) {
        StringBuilder sb = new StringBuilder(" IN (");
        if (args.size() > 0) {
            for (Object obj : args) {
                sb.append("'").append(obj).append("'").append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.append(")").toString();
    }

    public static List<String> parseArrayField(String fieldValue) {
        if (fieldValue != null) {
            return Arrays.asList(fieldValue.split(","));
        }
        return null;
    }

}
