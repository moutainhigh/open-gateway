package org.open.gateway.test;

import org.junit.jupiter.api.Test;
import org.open.gateway.BaseTest;
import org.open.gateway.common.utils.CollectionUtil;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.route.entity.token.TokenUser;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StopWatch;

import java.util.stream.Stream;

/**
 * Created by miko on 2020/8/18.
 *
 * @author MIKO
 */
public class RedisSerializerTest extends BaseTest {

    private final int test_count = 100000;

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
        StopWatch sw1 = newStopWatch();
        StopWatch sw2 = newStopWatch();
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
        printSummary(serializer.getClass().getSimpleName() + "序列化", sw1);
        printSummary(serializer.getClass().getSimpleName() + "反序列化", sw2);
    }

    public void testSerializer(RedisSerializer serializer) {
        StopWatch sw1 = newStopWatch();
        StopWatch sw2 = newStopWatch();
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
        printSummary(serializer.getClass().getSimpleName() + "序列化", sw1);
        printSummary(serializer.getClass().getSimpleName() + "反序列化", sw2);
    }

    private TokenUser newTokenUser() {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClientId(StringUtil.randomLetter(6));
        tokenUser.setAuthorities(CollectionUtil.newHashSet("admin"));
        tokenUser.setScopes(CollectionUtil.newHashSet("all"));
        return tokenUser;
    }

}
