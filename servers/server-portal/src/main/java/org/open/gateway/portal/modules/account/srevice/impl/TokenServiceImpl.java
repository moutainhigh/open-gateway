package org.open.gateway.portal.modules.account.srevice.impl;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.IdUtil;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.portal.modules.account.srevice.TokenService;
import org.open.gateway.portal.security.AccountDetails;
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

    private final Duration duration;

    private final StringRedisTemplate redisTemplate;


    public TokenServiceImpl(@Value("${token.duration.minutes:30}") long tokenDurationMinutes, StringRedisTemplate redisTemplate) {
        // token持续时间
        this.duration = Duration.ofMinutes(tokenDurationMinutes);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateToken(AccountDetails account) {
        String accountKey = getRedisAccountKey(account.getAccount());
        // 查询帐户是否存在token
        String token = redisTemplate.opsForValue().get(accountKey);
        if (token == null) {
            token = IdUtil.uuid();
            Boolean setAccountResult = this.setIfAbsent(accountKey, token);
            log.info("store account token finished. account:{} token:{} result:{} minutes.", account.getAccount(), token, setAccountResult);
            // 只有设置成功帐户token才有资格存储token
            if (setAccountResult != null && setAccountResult) {
                log.info("generated token is:{}", token);
                Boolean setTokenResult = this.setIfAbsentJson(getRedisTokenKey(token), account);
                log.info("store token into redis finished. result:{} duration:{}s minutes.", setTokenResult, duration.getSeconds());
                if (setTokenResult == null || !setTokenResult) {
                    throw new IllegalStateException("store token failed");
                }
            } else {
                log.info("set account:{} token failed", account);
                // 重新获取
                token = redisTemplate.opsForValue().get(accountKey);
                log.info("get account:{} new token is:{}", account, token);
                if (token == null) {
                    throw new IllegalStateException("new token is null");
                }
            }
        }
        log.info("account:{} result token:{}", account.getAccount(), token);
        return token;
    }


    @Override
    public void updateToken(AccountDetails accountDetails) {
        String accountKey = getRedisAccountKey(accountDetails.getAccount());
        String token = redisTemplate.opsForValue().get(accountKey);
        if (token == null) {
            throw new IllegalStateException("token is null");
        }
        String tokenKey = getRedisTokenKey(token);
        setJson(tokenKey, accountDetails);
        log.info("update token finished. new token value is:{}", accountDetails);
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
    public AccountDetails loadTokenUser(String token) {
        String tokenValue = redisTemplate.opsForValue().get(getRedisTokenKey(token));
        if (tokenValue == null) {
            return null;
        }
        return JSON.parse(tokenValue, AccountDetails.class);
    }

    private void set(String key, String o) {
        redisTemplate.opsForValue().set(key, o, duration);
    }

    private void setJson(String key, Object o) {
        this.set(key, JSON.toJSONString(o));
    }

    private Boolean setIfAbsent(String key, String o) {
        return redisTemplate.opsForValue().setIfAbsent(key, o, duration);
    }

    private Boolean setIfAbsentJson(String key, Object o) {
        return this.setIfAbsent(key, JSON.toJSONString(o));
    }

    private String getRedisTokenKey(String token) {
        return "portal_token:" + token;
    }

    private String getRedisAccountKey(String account) {
        return "portal_account:" + account;
    }

}
