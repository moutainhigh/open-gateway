package org.open.gateway.portal.modules.account.controller;

import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.open.gateway.portal.constants.EndPoints;
import org.open.gateway.portal.constants.ResultCode;
import org.open.gateway.portal.exception.*;
import org.open.gateway.portal.modules.account.controller.vo.*;
import org.open.gateway.portal.modules.account.srevice.AccountResourceService;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountResourceBO;
import org.open.gateway.portal.security.AccountDetails;
import org.open.gateway.portal.utils.BizUtil;
import org.open.gateway.portal.utils.ServletRequestUtil;
import org.open.gateway.portal.vo.Result;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final AccountResourceService accountResourceService;

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

    @PostMapping(EndPoints.ACCOUNT_REGISTER)
    public Result register(@Valid @RequestBody AccountRegisterRequest request, HttpServletRequest servletRequest, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotAvailableException, AccountAlreadyExistsException {
        String ip = ServletRequestUtil.getIpFromRequest(servletRequest);
        log.info("request ip is:{}", ip);
        BaseAccountBO accountBO = accountService.register(request.getAccount(), request.getPassword(), request.getPhone(), request.getEmail(), request.getNote(), ip, account.getAccount());
        AccountRegisterResponse response = toAccountRegisterResponse(accountBO);
        // 生成返回对象
        return Result.data(response).ok();
    }

    @PostMapping(EndPoints.ACCOUNT_LOGOUT)
    public Result logout(@AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        accountService.logout(account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:post:update')")
    @PostMapping(EndPoints.ACCOUNT_UPDATE)
    public Result update(@Valid @RequestBody AccountUpdateRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException, AccountNotAvailableException {
        accountService.update(request.getAccount(), request.getPassword(), request.getPhone(), request.getEmail(), request.getNote(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:post:enable')")
    @PostMapping(EndPoints.ACCOUNT_ENABLE)
    public Result enable(@Valid @RequestBody AccountEnableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException, AccountNotAvailableException {
        accountService.enable(request.getAccount(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:post:disable')")
    @PostMapping(EndPoints.ACCOUNT_DISABLE)
    public Result disable(@Valid @RequestBody AccountDisableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException, AccountNotAvailableException {
        accountService.disable(request.getAccount(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:post:delete')")
    @PostMapping(EndPoints.ACCOUNT_DELETE)
    public Result delete(@Valid @RequestBody AccountDeleteRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException {
        accountService.delete(request.getAccount(), account.getAccount());
        return Result.ok();
    }

    @PostMapping(EndPoints.ACCOUNT_RESOURCES)
    public Result resources(@AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        List<BaseAccountResourceBO> resources = accountResourceService.queryResourcesByAccount(account.getAccount());
        return Result.data(resources).ok();
    }

    @PreAuthorize("#account.hasPermission('account:post:pageList')")
    @PostMapping(EndPoints.ACCOUNT_PAGE_LIST)
    public Result pageList(@Valid @RequestBody AccountPageListRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        // TODO
        return Result.ok();
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
