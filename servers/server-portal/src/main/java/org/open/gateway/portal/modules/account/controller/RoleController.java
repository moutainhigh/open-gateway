package org.open.gateway.portal.modules.account.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.EndPoints;
import org.open.gateway.portal.exception.role.RoleNotExistsException;
import org.open.gateway.portal.modules.account.controller.vo.AccountRoleDisableRequest;
import org.open.gateway.portal.modules.account.controller.vo.AccountRoleEnableRequest;
import org.open.gateway.portal.modules.account.controller.vo.AccountRoleSaveRequest;
import org.open.gateway.portal.modules.account.controller.vo.AccountRolesRequest;
import org.open.gateway.portal.modules.account.service.AccountRoleService;
import org.open.gateway.portal.modules.account.service.bo.BaseRoleBO;
import org.open.gateway.portal.security.AccountDetails;
import org.open.gateway.portal.vo.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by miko on 10/27/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@RestController
public class RoleController {

    private final AccountRoleService accountRoleService;

    @PreAuthorize("#account.hasPermission('account:role:pages:post')")
    @PostMapping(EndPoints.ACCOUNT_ROLE_PAGES)
    public Result roles(@Valid @RequestBody AccountRolesRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        List<BaseRoleBO> baseRoleBOS = accountRoleService.queryRolesByAccount(request.getAccount());
        return Result.data(baseRoleBOS).ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:save:post')")
    @PostMapping(EndPoints.ACCOUNT_ROLE_SAVE)
    public Result save(@Valid @RequestBody AccountRoleSaveRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        accountRoleService.saveRole(request.getRoleCode(), request.getRoleName(), request.getNote(), account.getAccount(), request.getResourceIds());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:enable:post')")
    public Result enable(@Valid @RequestBody AccountRoleEnableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.enable(request.getRoleCode(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:disable:post')")
    public Result disable(@Valid @RequestBody AccountRoleDisableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.disable(request.getRoleCode(), account.getAccount());
        return Result.ok();
    }

}
