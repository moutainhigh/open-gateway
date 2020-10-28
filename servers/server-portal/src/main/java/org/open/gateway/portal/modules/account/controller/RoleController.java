package org.open.gateway.portal.modules.account.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.exception.account.RoleNotExistsException;
import org.open.gateway.portal.modules.account.controller.vo.*;
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
    @PostMapping(Endpoints.ACCOUNT_ROLE_PAGES)
    public Result roles(@Valid @RequestBody RolesRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        List<BaseRoleBO> baseRoleBOS = accountRoleService.queryRolesByAccount(request.getAccount());
        return Result.data(baseRoleBOS).ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:save:post')")
    @PostMapping(Endpoints.ACCOUNT_ROLE_SAVE)
    public Result save(@Valid @RequestBody RoleSaveRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        accountRoleService.save(request.getRoleCode(), request.getRoleName(), request.getNote(), account.getAccount(), request.getResourceIds());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:enable:post')")
    @PostMapping(Endpoints.ACCOUNT_ROLE_ENABLE)
    public Result enable(@Valid @RequestBody RoleEnableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.enable(request.getRoleCode(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:disable:post')")
    @PostMapping(Endpoints.ACCOUNT_ROLE_DISABLE)
    public Result disable(@Valid @RequestBody RoleDisableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.disable(request.getRoleCode(), account.getAccount());
        return Result.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:delete:post')")
    @PostMapping(Endpoints.ACCOUNT_ROLE_DELETE)
    public Result delete(@Valid @RequestBody RoleDeleteRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.delete(request.getRoleCode(), account.getAccount());
        return Result.ok();
    }

}
