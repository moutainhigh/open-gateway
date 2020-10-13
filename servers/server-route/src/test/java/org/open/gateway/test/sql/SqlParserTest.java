package org.open.gateway.test.sql;

import org.junit.jupiter.api.Test;
import org.open.gateway.BaseTest;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

/**
 * Created by miko on 10/13/20.
 *
 * @author MIKO
 */
public class SqlParserTest extends BaseTest {

    @Test
    public void benchmarkSqlParser() {
        StopWatch sw = newStopWatch();
        int count = 1000000;
        SqlParser sqlParser = new DefaultSqlParser();
        for (int i = 0; i < count; i++) {
            sw.start();
            parse(sqlParser);
            sw.stop();
        }
        printSummary("sqlParse1", sw);
    }

    private void parse(SqlParser sqlParser) {
        String msg = "parse sql error";
        Assert.isTrue("select * from table".equals(sqlParser.parse("select * from table")), msg);
        Assert.isTrue("select * from table1".equals(sqlParser.parse("select * from {0}", "table1")), msg);
        Assert.isTrue("select * from table1 inner join table2".equals(sqlParser.parse("select * from {0} inner join {1}", "table1", "table2")), msg);
        Assert.isTrue("select * from table2 inner join table1".equals(sqlParser.parse("select * from {1} inner join {0}", "table1", "table2")), msg);
        Assert.isTrue("select * from table1 inner join table1".equals(sqlParser.parse("select * from {0} inner join {0}", "table1", "table2")), msg);
        Assert.isTrue("select name,code from table1".equals(sqlParser.parse("select {0},{1} from table1", "name", "code")), msg);
        Assert.isTrue("select * from table1".equals(sqlParser.parse("{0} * from {1}", "select", "table1")), msg);
        Assert.isTrue("select * from {".equals(sqlParser.parse("select * from {", "table1", "table2")), msg);
        Assert.isTrue("select * from {0".equals(sqlParser.parse("select * from {0", "table1", "table2")), msg);
        Assert.isTrue("select * from {}".equals(sqlParser.parse("select * from {}", "table1", "table2")), msg);
        Assert.isTrue("select * from {table1}".equals(sqlParser.parse("select * from {{0}}", "table1", "table2")), msg);
    }


}
