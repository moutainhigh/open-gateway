package org.open.gateway.portal.modules.account.controller;

import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.open.gateway.portal.constants.EndPoints;
import org.open.gateway.portal.constants.ResultCode;
import org.open.gateway.portal.exception.*;
import org.open.gateway.portal.modules.account.controller.vo.*;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.bo.AccountResourceBO;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.utils.BizUtil;
import org.open.gateway.portal.utils.ServletRequestUtil;
import org.open.gateway.portal.vo.Result;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping(EndPoints.ACCOUNT_LOGIN)
    public Result login(@Valid @RequestBody AccountLoginRequest request) throws AccountPasswordInvalidException, AccountNotExistsException, AccountNotAvailableException {
        // 校验请求
        checkAccountLoginRequest(request);
        //  登录
        String token = accountService.login(request.getAccount(), request.getPassword());
        // 生成返回对象
        AccountLoginResponse response = buildLoginResponse(token);
        return Result.data(response).ok();
    }

    @PostMapping(EndPoints.ACCOUNT_LOGOUT)
    public Result logout(@Valid @RequestBody AccountLogoutRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) BaseAccountBO account) {
        accountService.logout(account.getAccount(), request.getToken());
        return Result.ok();
    }

    @PostMapping(EndPoints.ACCOUNT_REGISTER)
    public Result register(@Valid @RequestBody AccountRegisterRequest request, HttpServletRequest servletRequest) throws AccountNotAvailableException, AccountExistsException {
        String ip = ServletRequestUtil.getIpFromRequest(servletRequest);
        log.info("Request ip is:{}", ip);
        BaseAccountBO accountBO = accountService.register(request.getAccount(), request.getPassword(), request.getPhone(), request.getEmail(), request.getNote(), ip);
        AccountRegisterResponse response = toAccountRegisterResponse(accountBO);
        // 生成返回对象
        return Result.data(response).ok();
    }

    @PostMapping(EndPoints.ACCOUNT_RESOURCES)
    public Result accountResources(@AuthenticationPrincipal(errorOnInvalidType = true) BaseAccountBO account) {
        List<AccountResourceBO> resources = accountService.queryResourcesByAccount(account.getAccount());
        return Result.data(resources).ok();
    }

    private AccountRegisterResponse toAccountRegisterResponse(BaseAccountBO accountBO) {
        AccountRegisterResponse response = new AccountRegisterResponse();
        response.setId(accountBO.getId());
        response.setAccount(accountBO.getAccount());
        response.setPassword(accountBO.getPassword());
        response.setRegisterIp(accountBO.getRegisterIp());
        response.setStatus(accountBO.getStatus());
        response.setPhone(accountBO.getPhone());
        response.setEmail(accountBO.getEmail());
        response.setNote(accountBO.getNote());
        return response;
    }

    private void checkAccountLoginRequest(AccountLoginRequest request) {
        // 校验请求日期是否过期
        if (LocalDateTime.now().minusMinutes(30).isAfter(request.getRequestTime())) {
            throw new ResultException(ResultCode.ACCOUNT_LOGIN_REQUEST_EXPIRED);
        }
        // 明文
        String plainPassword = BizUtil.decodePassword(request.getPassword(), SecureUtil.md5(request.getRequestTime().format(DateTimeFormatters.yyyy_MM_dd_HH_mm_ss)));
        log.info("secret password is:{} plain password is:{}", request.getPassword(), plainPassword);
        request.setPassword(plainPassword);
    }

    private AccountLoginResponse buildLoginResponse(String token) {
        AccountLoginResponse accountLoginResponse = new AccountLoginResponse();
        accountLoginResponse.setToken(token);
        return accountLoginResponse;
    }

}
