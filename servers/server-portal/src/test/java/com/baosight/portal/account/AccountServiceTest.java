package com.baosight.portal.account;

import cn.hutool.crypto.SecureUtil;
import com.baosight.portal.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.open.gateway.portal.exception.account.AccountAlreadyExistsException;
import org.open.gateway.portal.exception.account.AccountNotAvailableException;
import org.open.gateway.portal.exception.account.AccountNotExistsException;
import org.open.gateway.portal.exception.account.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.controller.vo.AccountLoginRequest;
import org.open.gateway.portal.modules.account.service.AccountService;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountBO;
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
public class AccountServiceTest extends BaseSpringTest {

    private final String account = "junit_test";
    private final String password = "admin123";
    private final String operator = "junit";

    @Autowired
    AccountService accountService;

    // 注册账户
    private BaseAccountBO registerAccount() throws AccountNotAvailableException, AccountAlreadyExistsException {
        BaseAccountBO accountBO = accountService.register(account, password, "17521125571", "oni-miko@outlook.com", "系统管理员", "10.60.86.128", operator);
        log.info("register account is:{}", accountBO);
        return accountBO;
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterAndLogin() throws AccountAlreadyExistsException, AccountNotAvailableException, AccountPasswordInvalidException, AccountNotExistsException {
        // 注册
        BaseAccountBO accountBO = registerAccount();
        Assert.notNull(accountBO.getId(), "account id is null");
        // 登录
        String token = accountService.login(account, password);
        log.info("login finished token is:{}", token);
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterAndUpdate() throws AccountNotExistsException, AccountNotAvailableException, AccountAlreadyExistsException {
        // 注册
        BaseAccountBO accountBO = registerAccount();
        Assert.notNull(accountBO.getId(), "account id is null");
        // 修改
        String phone = "17521125570";
        String email = "834563385@qq.com";
        String note = "new note";
        accountService.update(account, null, phone, email, note, operator, null);
        BaseAccountBO accountAfterUpdate = accountService.queryBaseAccount(account);
        Assert.notNull(accountAfterUpdate, "account no found");
        Assert.isTrue(phone.equals(accountAfterUpdate.getPhone()), "phone update failed. not equals");
        Assert.isTrue(email.equals(accountAfterUpdate.getEmail()), "email update failed. not equals");
        Assert.isTrue(note.equals(accountAfterUpdate.getNote()), "note update failed. not equals");
        Assert.isTrue(accountBO.getPassword().equals(accountAfterUpdate.getPassword()), "password changed after update. should equals before update.");
        Assert.isTrue(accountBO.getSalt().equals(accountAfterUpdate.getSalt()), "salt changed after update. should equals before update.");
    }

    @Test
    @Disabled
    public void buildLoginRequest() {
        LocalDateTime requestTime = LocalDateTime.now();
        String requestTimeString = requestTime.format(DateTimeFormatters.yyyy_MM_dd_HH_mm_ss);
        log.info("requestTimeString:{}", requestTimeString);
        String password = BizUtil.encodePassword("admin123", SecureUtil.md5(requestTimeString));
        AccountLoginRequest request = new AccountLoginRequest();
        request.setAccount("admin");
        request.setPassword(password);
        request.setRequestTime(requestTime);
        String requestBody = JSON.toJSONString(request);
        log.info("login request body:{}", requestBody);
    }

}
