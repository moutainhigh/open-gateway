package com.baosight.portal;

import org.springframework.util.StopWatch;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    public String currentThreadName() {
        return Thread.currentThread().getName();
    }

    public static String getUUIDWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static final char[] keyGen = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789".toCharArray();

    public static String generateRandomStr(String prefix, int len) {
        if (prefix == null || prefix.isEmpty()) {
            return generateRandomStr(keyGen, len);
        }
        int diff = len - prefix.length();
        if (diff > 0) {
            return prefix + generateRandomStr(keyGen, diff);
        }
        if (diff < 0) {
            return prefix.substring(0, len);
        }
        return prefix;
    }

    public static String generateRandomStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(keyGen[getRandomInt(keyGen.length)]);
        }
        return sb.toString();
    }

    public static String generateRandomStr(char[] keyGen, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(keyGen[getRandomInt(keyGen.length)]);
        }
        return sb.toString();
    }


    public static int getRandomInt(int max) {
        return new Random().nextInt(max);
    }

    public static double getRandomDouble(int max) {
        return new Random().nextDouble() * max;
    }

    public static <T> boolean objectEq(T o1, T o2) {
        return objectEq(o1, o2, null);
    }

    public static <T> boolean objectEq(T o1, T o2, Predicate<Field> filter) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        return Stream.of(o1.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && (filter == null || filter.test(field))) // 忽略静态属性
                .allMatch(field -> {
                    Object value1;
                    Object value2;
                    try {
                        value1 = field.get(o1);
                        value2 = field.get(o2);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (value1 == value2) {
                        return true;
                    }
                    if (value1 == null) {
                        return false;
                    }
                    if (value1.equals(value2)) {
                        return true;
                    }
                    if (value1 instanceof Comparable) {
                        //noinspection unchecked
                        return ((Comparable<Object>) value1).compareTo(value2) == 0;
                    }
                    return false;
                });
    }

}
