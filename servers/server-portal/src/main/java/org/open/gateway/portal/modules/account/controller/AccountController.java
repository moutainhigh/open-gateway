package org.open.gateway.portal.modules.account.controller;

import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.constants.ResultCode;
import org.open.gateway.portal.exception.ResultException;
import org.open.gateway.portal.exception.account.AccountAlreadyExistsException;
import org.open.gateway.portal.exception.account.AccountNotAvailableException;
import org.open.gateway.portal.exception.account.AccountNotExistsException;
import org.open.gateway.portal.exception.account.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.controller.vo.*;
import org.open.gateway.portal.modules.account.service.AccountService;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountBO;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountQuery;
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
import java.util.stream.Collectors;

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

    @PostMapping(Endpoints.ACCOUNT_LOGIN)
    public Result login(@Valid @RequestBody AccountLoginRequest request) throws AccountPasswordInvalidException, AccountNotExistsException, AccountNotAvailableException {
        // 校验请求
        checkAccountLoginRequest(request);
        //  登录
        String token = accountService.login(request.getAccount(), request.getPassword());
        // 生成返回对象
        AccountLoginResponse response = buildLoginResponse(token);
        return Result.data(response).ok();
    }

    @PostMapping(Endpoints.ACCOUNT_REGISTER)
    public Result register(@Valid @RequestBody AccountRegisterRequest request, HttpServletRequest servletRequest, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotAvailableException, AccountAlreadyExistsException {
        String ip = ServletRequestUtil.getIpFromRequest(servletRequest);
        log.info("request ip is:{}", ip);
        // 注册
        BaseAccountBO accountBO = accountService.register(request.getAccount(), request.getPassword(), request.getPhone(), request.getEmail(), request.getNote(), ip, account.getAccount());
        // 生成返回对象
        AccountRegisterResponse response = toAccountRegisterResponse(accountBO);
        return Result.data(response).ok();
    }

    @PostMapping(Endpoints.ACCOUNT_LOGOUT)
    public Result logout(@AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        accountService.logout(account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:update:post')")
    @PostMapping(Endpoints.ACCOUNT_UPDATE)
    public Result update(@Valid @RequestBody AccountUpdateRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException, AccountNotAvailableException {
        accountService.update(request.getAccount(), request.getPassword(), request.getPhone(), request.getEmail(), request.getNote(), account.getAccount(), request.getRoleIds());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:enable:post')")
    @PostMapping(Endpoints.ACCOUNT_ENABLE)
    public Result enable(@Valid @RequestBody AccountEnableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException, AccountNotAvailableException {
        accountService.enable(request.getAccount(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:disable:post')")
    @PostMapping(Endpoints.ACCOUNT_DISABLE)
    public Result disable(@Valid @RequestBody AccountDisableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException, AccountNotAvailableException {
        accountService.disable(request.getAccount(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:delete:post')")
    @PostMapping(Endpoints.ACCOUNT_DELETE)
    public Result delete(@Valid @RequestBody AccountDeleteRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AccountNotExistsException {
        accountService.delete(request.getAccount(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:pages:post')")
    @PostMapping(Endpoints.ACCOUNT_PAGES)
    public Result pages(@Valid @RequestBody AccountPagesRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        BaseAccountQuery query = toBaseAccountQuery(request);
        Page<?> page = request.startPage();
        List<BaseAccountBO> accounts = accountService.queryBaseAccounts(query);
        List<AccountPagesResponse> responses = accounts.stream()
                .map(this::toAccountPageListResponse)
                .collect(Collectors.toList());
        log.info("response num:{}", responses.size());
        return Result.data(responses).pageInfo(page).ok();
    }

    private BaseAccountQuery toBaseAccountQuery(AccountPagesRequest request) {
        BaseAccountQuery query = new BaseAccountQuery();
        query.setAccount(request.getAccount());
        query.setStatus(request.getStatus());
        query.setPhone(request.getPhone());
        query.setEmail(request.getEmail());
        query.setCreateTimeBegin(request.getCreateDateBegin());
        query.setCreateTimeEnd(request.getCreateDateEnd());
        return query;
    }

    private AccountPagesResponse toAccountPageListResponse(BaseAccountBO accountBO) {
        AccountPagesResponse response = new AccountPagesResponse();
        response.setId(accountBO.getId());
        response.setAccount(accountBO.getAccount());
        response.setStatus(accountBO.getStatus());
        response.setPhone(accountBO.getPhone());
        response.setEmail(accountBO.getEmail());
        response.setNote(accountBO.getNote());
        response.setCreateTime(accountBO.getCreateTime());
        response.setCreatePerson(accountBO.getCreatePerson());
        response.setUpdateTime(accountBO.getUpdateTime());
        response.setUpdatePerson(accountBO.getUpdatePerson());
        response.setRegisterIp(accountBO.getRegisterIp());
        return response;
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
