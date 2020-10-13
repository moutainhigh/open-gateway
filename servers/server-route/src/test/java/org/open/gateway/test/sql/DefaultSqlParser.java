package org.open.gateway.test.sql;

/**
 * Created by miko on 10/13/20.
 *
 * @author MIKO
 */
public class DefaultSqlParser implements SqlParser {

    private static final char open = '{';
    private static final char close = '}';

    /**
     * 解析sql
     *
     * @param sql 原始sql
     * @return 解析后的sql
     */
    @Override
    public String parse(String sql, Object... args) {
        if (args == null || args.length == 0) {
            return sql;
        }
        StringBuilder sb = new StringBuilder();
        int processBegin = -1;
        char[] chars = sql.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            // 当前字符等于open
            if (c == open) {
                // 如果已经是处理中, 补全字符
                if (processBegin != -1) {
                    sb.append(sql, processBegin, i);
                }
                processBegin = i; // 重置下标
            }
            // 当前是处理中
            else if (processBegin != -1) {
                // 当前字符等于close
                if (c == close) {
                    // 开始与结束下标相差大于1进行替换参数
                    if (processBegin + 1 < i) {
                        int index = Integer.parseInt(sql.substring(processBegin + 1, i));
                        if (index >= args.length) {
                            throw new IndexOutOfBoundsException("Index can not large than args size:" + args.length);
                        }
                        Object arg = args[index];
                        sb.append(arg);
                    } else {
                        sb.append(sql, processBegin, i).append(c);
                    }
                    processBegin = -1; // 重置下标
                }
            } else {
                sb.append(c);
            }
        }
        if (processBegin != -1) {
            sb.append(sql.substring(processBegin));
        }
        return sb.toString();
    }

}
