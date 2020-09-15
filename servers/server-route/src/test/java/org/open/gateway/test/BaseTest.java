package org.open.gateway.test;

import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by miko on 2020/8/20.
 *
 * @author MIKO
 */
public class BaseTest {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public StopWatch newStopWatch() {
        return new StopWatch();
    }

    public void printSummary(String prefix, StopWatch sw) {
        System.out.printf("%s 总耗时[%s ms] 次数[%s] 平均耗时[%s us].%n", prefix, sw.getTotalTimeMillis(), sw.getTaskCount(), BigDecimal.valueOf(sw.getTotalTimeNanos()).divide(BigDecimal.valueOf(sw.getTaskCount()), 2, RoundingMode.HALF_UP));
    }

    public String currentThreadName(){
        return Thread.currentThread().getName();
    }

}
