package org.open.gateway.test;

import open.gateway.common.utils.JSON;
import open.gateway.common.utils.StringUtil;
import org.junit.jupiter.api.Test;
import org.open.gateway.route.entity.token.TokenUser;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by miko on 2020/8/18.
 *
 * @author MIKO
 */
public class RedisSerializerTest {

    private final int test_count = 10000;

    @Test
    public void testJdkSerializationRedisSerializer() {
        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(RedisSerializerTest.class.getClassLoader());
        testSerializer(jdkSerializer);
    }

    @Test
    public void testJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<TokenUser> jsonSerializer = new Jackson2JsonRedisSerializer<>(TokenUser.class);
        testSerializer(jsonSerializer);
    }

    @Test
    public void testStringSerializer() {
        RedisSerializer<String> serializer = StringRedisSerializer.UTF_8;
        StopWatch sw1 = new StopWatch();
        StopWatch sw2 = new StopWatch();
        Stream.generate(this::newTokenUser)
                .limit(test_count)
                .forEach(tokenUser -> {
                    sw1.start();
                    byte[] bytes = serializer.serialize(JSON.toJSONString(tokenUser));
                    sw1.stop();

                    sw2.start();
                    TokenUser tu = JSON.parse(serializer.deserialize(bytes), TokenUser.class);
                    sw2.stop();
                });
        System.out.printf("%s序列化总耗时[%s ms] 次数[%s] 平均耗时[%s us].%n", serializer.getClass().getSimpleName(), sw1.getTotalTimeMillis(), sw1.getTaskCount(), BigDecimal.valueOf(sw1.getTotalTimeNanos()).divide(BigDecimal.valueOf(sw1.getTaskCount()), 2, RoundingMode.HALF_UP));
        System.out.printf("%s反序列化总耗时[%s ms] 次数[%s] 平均耗时[%s us].%n", serializer.getClass().getSimpleName(), sw2.getTotalTimeMillis(), sw2.getTaskCount(), BigDecimal.valueOf(sw2.getTotalTimeNanos()).divide(BigDecimal.valueOf(sw2.getTaskCount()), 2, RoundingMode.HALF_UP));
    }

    public void testSerializer(RedisSerializer serializer) {
        StopWatch sw1 = new StopWatch();
        StopWatch sw2 = new StopWatch();
        Stream.generate(this::newTokenUser)
                .limit(test_count)
                .forEach(tokenUser -> {
                    sw1.start();
                    byte[] bytes = serializer.serialize(tokenUser);
                    sw1.stop();

                    sw2.start();
                    TokenUser tu = (TokenUser) serializer.deserialize(bytes);
                    sw2.stop();
                });
        System.out.printf("%s序列化总耗时[%s ms] 次数[%s] 平均耗时[%s us].%n", serializer.getClass().getSimpleName(), sw1.getTotalTimeMillis(), sw1.getTaskCount(), BigDecimal.valueOf(sw1.getTotalTimeNanos()).divide(BigDecimal.valueOf(sw1.getTaskCount()), 2, RoundingMode.HALF_UP));
        System.out.printf("%s反序列化总耗时[%s ms] 次数[%s] 平均耗时[%s us].%n", serializer.getClass().getSimpleName(), sw2.getTotalTimeMillis(), sw2.getTaskCount(), BigDecimal.valueOf(sw2.getTotalTimeNanos()).divide(BigDecimal.valueOf(sw2.getTaskCount()), 2, RoundingMode.HALF_UP));
    }

    private TokenUser newTokenUser() {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClientId(StringUtil.randomLetterString(6));
        tokenUser.setAuthorities(Arrays.asList("admin"));
        tokenUser.setScopes(Arrays.asList("all"));
        return tokenUser;
    }

}
