package org.open.gateway.test;

import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by miko on 2020/8/18.
 *
 * @author MIKO
 */
public class StopWatchUtil {

    public static StopWatch getStopWatch() {
        return new StopWatch();
    }

    public static void printSummary(String prefix, StopWatch sw) {
        System.out.printf("%s 总耗时[%s ms] 次数[%s] 平均耗时[%s us].%n", prefix, sw.getTotalTimeMillis(), sw.getTaskCount(), BigDecimal.valueOf(sw.getTotalTimeNanos()).divide(BigDecimal.valueOf(sw.getTaskCount()), 2, RoundingMode.HALF_UP));

    }

}
