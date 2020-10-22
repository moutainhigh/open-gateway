package org.open.gateway.portal.modules.account.srevice.impl;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.IdUtil;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.portal.modules.account.srevice.TokenService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Created by miko on 10/21/20.
 *
 * @author MIKO
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final long tokenDurationMinutes;
    private final StringRedisTemplate redisTemplate;

    public TokenServiceImpl(@Value("${token.duration.minutes:30}") long tokenDurationMinutes, StringRedisTemplate redisTemplate) {
        this.tokenDurationMinutes = tokenDurationMinutes;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateToken(BaseAccountBO accountBO) {
        String accountKey = getRedisAccountKey(accountBO.getAccount());
        // token持续时间
        Duration duration = Duration.ofMinutes(tokenDurationMinutes);
        // 查询帐户是否存在token
        String token = redisTemplate.opsForValue().get(accountKey);
        if (token == null) {
            token = IdUtil.uuid();
            Boolean setAccountResult = redisTemplate.opsForValue().setIfAbsent(accountKey, token, duration);
            log.info("store account token finished. account:{} token:{} result:{} minutes.", accountBO.getAccount(), token, setAccountResult);
            // 只有设置成功帐户token才有资格存储token
            if (setAccountResult != null && setAccountResult) {
                log.info("generated token is:{}", token);
                Boolean setTokenResult = redisTemplate.opsForValue().setIfAbsent(getRedisTokenKey(token), JSON.toJSONString(accountBO), duration);
                log.info("store token into redis finished. result:{} duration:{} minutes.", setTokenResult, tokenDurationMinutes);
                if (setTokenResult == null || !setTokenResult) {
                    throw new IllegalStateException("store token failed");
                }
            } else {
                log.info("set account:{} token failed", accountBO);
                // 重新获取
                token = redisTemplate.opsForValue().get(accountKey);
                log.info("get account:{} new token is:{}", accountBO, token);
            }
        }
        log.info("account:{} result token:{}", accountBO.getAccount(), token);
        return token;
    }

    @Override
    public Boolean deleteToken(String account) {
        String accountKey = getRedisAccountKey(account);
        String token = redisTemplate.opsForValue().get(accountKey);
        log.info("get token:{} by account:{}", token, account);
        Boolean delAccountResult = redisTemplate.delete(accountKey);
        log.info("delete accountKey:{} result:{}", accountKey, delAccountResult);
        String tokenKey = getRedisTokenKey(token);
        Boolean delTokenResult = redisTemplate.delete(tokenKey);
        log.info("delete tokenKey:{} result:{}", tokenKey, delTokenResult);
        return delAccountResult != null && delAccountResult && delTokenResult != null && delTokenResult;
    }

    @Override
    public BaseAccountBO loadTokenUser(String token) {
        String tokenValue = redisTemplate.opsForValue().get(getRedisTokenKey(token));
        if (tokenValue == null) {
            return null;
        }
        return JSON.parse(tokenValue, BaseAccountBO.class);
    }

    private String getRedisTokenKey(String token) {
        return "portal_token:" + token;
    }

    private String getRedisAccountKey(String account) {
        return "portal_account:" + account;
    }

}
