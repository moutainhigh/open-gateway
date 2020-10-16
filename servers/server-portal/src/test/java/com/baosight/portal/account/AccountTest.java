package com.baosight.portal.account;

import cn.hutool.crypto.SecureUtil;
import com.baosight.portal.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.open.gateway.portal.exception.AccountExistsException;
import org.open.gateway.portal.exception.AccountNotAvailableException;
import org.open.gateway.portal.exception.AccountNotExistsException;
import org.open.gateway.portal.exception.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.controller.vo.AccountLoginRequest;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Slf4j
public class AccountTest extends BaseSpringTest {

    @Autowired
    AccountService accountService;

    @Test
    @Transactional
    @Rollback
    public void testRegisterAndLogin() throws AccountExistsException, AccountNotAvailableException, AccountPasswordInvalidException, AccountNotExistsException {
        String account = "junit_test";
        String password = "admin123";
        BaseAccountBO accountBO = accountService.register(account, password, "17521125571", "oni-miko@outlook.com", "系统管理员", "10.60.86.128");
        log.info("register account is:{}", accountBO);
        Assert.notNull(accountBO.getId(), "account id is null");
        String token = accountService.login(account, password);
        log.info("login finished token is:{}", token);
    }

    @Test
    public void buildLoginRequest() {
        LocalDateTime requestTime = LocalDateTime.now();
        String requestTimeString = requestTime.format(DateTimeFormatters.yyyy_MM_dd_HH_mm_ss);
        log.info("requestTimeString:{}", requestTimeString);
        String password = BizUtil.encodePassword("password", SecureUtil.md5(requestTimeString));
        AccountLoginRequest request = new AccountLoginRequest();
        request.setAccount("admin");
        request.setPassword(password);
        request.setRequestTime(requestTime);
        String requestBody = JSON.toJSONString(request);
        log.info("login request body:{}", requestBody);
    }

}
