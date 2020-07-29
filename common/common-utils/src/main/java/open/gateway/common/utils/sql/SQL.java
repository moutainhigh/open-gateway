package open.gateway.common.utils.sql;

import java.util.Collection;

public class SQL extends AbstractSQL<SQL> {

    @Override
    public SQL getSelf() {
        return this;
    }

    public String format(Object... args) {
        return String.format(this.toString(), args);
    }

    public String WHERE_IN(String column, Collection<?> args) {
        if (sql().where.isEmpty()) {
            return this.toString() + " WHERE " + column + getWhereInString(args);
        } else {
            return this.toString() + " AND " + column + getWhereInString(args);
        }
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

}