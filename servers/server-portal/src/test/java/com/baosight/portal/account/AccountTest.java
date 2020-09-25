package com.baosight.portal.account;

import com.baosight.portal.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
    public void testRegisterAndLogin() {
        String account = "junit_test";
        String password = "admin123";
        BaseAccountBO accountBO = accountService.register(account, password, "17521125571", "oni-miko@outlook.com", "系统管理员", "10.60.86.128");
        log.info("register account is:{}", accountBO);
        Assert.notNull(accountBO.getId(), "account id is null");
        String token = accountService.login(account, password);
        log.info("login finished token is:{}", token);
    }

}
