package org.open.gateway.portal.modules.account.controller;

import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.EndPoints;
import org.open.gateway.portal.modules.account.controller.vo.AccountLoginRequest;
import org.open.gateway.portal.modules.account.controller.vo.AccountLoginResponse;
import org.open.gateway.portal.modules.account.controller.vo.AccountRegisterRequest;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.utils.BizUtil;
import org.open.gateway.portal.vo.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response login(@Valid AccountLoginRequest request) {
        // 校验请求
        checkAccountLoginRequest(request);
        //  登录
        String token = accountService.login(request.getAccount(), request.getPassword());
        // 生成返回对象
        AccountLoginResponse response = buildLoginResponse(token);
        return Response.data(response).ok();
    }

    @PostMapping(EndPoints.ACCOUNT_LOGIN)
    public Response register(@Valid AccountRegisterRequest request) {
        // 校验请求
        checkAccountRegisterRequest(request);
        // TODO
        // 生成返回对象
        return Response.data(null).ok();
    }

    private void checkAccountLoginRequest(AccountLoginRequest request) {
        // 校验请求日期是否过期
        if (LocalDateTime.now().minusMinutes(30).isBefore(request.getRequestTime())) {
            throw new IllegalStateException("request expired");
        }
        // 明文
        String plainPassword = BizUtil.decodePassword(request.getPassword(), SecureUtil.md5(request.getRequestTime().toString()));
        log.info("secret password is:{} plain password is:{}", request.getPassword(), plainPassword);
        request.setPassword(plainPassword);
    }

    private void checkAccountRegisterRequest(AccountRegisterRequest request) {

    }

    private AccountLoginResponse buildLoginResponse(String token) {
        AccountLoginResponse accountLoginResponse = new AccountLoginResponse();
        accountLoginResponse.setToken(token);
        return accountLoginResponse;
    }

}
