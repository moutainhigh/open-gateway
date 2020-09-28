package org.open.gateway.portal.modules.account.controller;

import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.open.gateway.portal.constants.EndPoints;
import org.open.gateway.portal.constants.ResultCode;
import org.open.gateway.portal.exception.BizException;
import org.open.gateway.portal.modules.account.controller.vo.*;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.utils.BizUtil;
import org.open.gateway.portal.utils.ServletRequestUtil;
import org.open.gateway.portal.vo.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

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
    public Response login(@Valid @RequestBody AccountLoginRequest request) {
        // 校验请求
        checkAccountLoginRequest(request);
        //  登录
        String token = accountService.login(request.getAccount(), request.getPassword());
        // 生成返回对象
        AccountLoginResponse response = buildLoginResponse(token);
        return Response.data(response).ok();
    }

    @PostMapping(EndPoints.ACCOUNT_LOGOUT)
    public Response logout(@Valid @RequestBody AccountLogoutRequest request) {
        accountService.logout(request.getToken());
        return Response.ok();
    }

    @PostMapping(EndPoints.ACCOUNT_REGISTER)
    public Response register(@Valid @RequestBody AccountRegisterRequest request, HttpServletRequest servletRequest) {
        // 校验请求
        checkAccountRegisterRequest(request);
        String ip = ServletRequestUtil.getIpFromRequest(servletRequest);
        log.info("Request ip is:{}", ip);
        BaseAccountBO accountBO = accountService.register(request.getAccount(), request.getPassword(), request.getPhone(), request.getEmail(), request.getNote(), ip);
        AccountRegisterResponse response = toAccountRegisterResponse(accountBO);
        // 生成返回对象
        return Response.data(response).ok();
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
            throw new IllegalStateException("request expired");
        }
        // 明文
        String plainPassword = BizUtil.decodePassword(request.getPassword(), SecureUtil.md5(request.getRequestTime().format(DateTimeFormatters.yyyy_MM_dd_HH_mm_ss)));
        log.info("secret password is:{} plain password is:{}", request.getPassword(), plainPassword);
        request.setPassword(plainPassword);
    }

    private void checkAccountRegisterRequest(AccountRegisterRequest request) {
        try {
            accountService.queryBaseAccountByCode(request.getAccount());
            throw new BizException(ResultCode.ACCOUNT_IS_EXISTS);
        } catch (BizException e) {
            // pass
        }
    }

    private AccountLoginResponse buildLoginResponse(String token) {
        AccountLoginResponse accountLoginResponse = new AccountLoginResponse();
        accountLoginResponse.setToken(token);
        return accountLoginResponse;
    }

}
