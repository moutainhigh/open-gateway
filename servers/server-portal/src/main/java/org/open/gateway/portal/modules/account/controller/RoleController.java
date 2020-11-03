package org.open.gateway.portal.modules.account.controller;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.exception.account.RoleNotExistsException;
import org.open.gateway.portal.modules.account.controller.vo.*;
import org.open.gateway.portal.modules.account.service.AccountRoleService;
import org.open.gateway.portal.modules.account.service.bo.BaseRoleBO;
import org.open.gateway.portal.security.AccountDetails;
import org.open.gateway.portal.vo.PageResponse;
import org.open.gateway.portal.vo.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping(Endpoints.ROLE_PAGES)
    public PageResponse<List<RolePagesResponse>> roles(@Valid @RequestBody RolePagesRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        Page<?> page = request.startPage();
        List<BaseRoleBO> baseRoleBOS = accountRoleService.queryRolesByAccount(request.getAccount());
        List<RolePagesResponse> responses = baseRoleBOS.stream()
                .map(this::toRolePagesResponse)
                .collect(Collectors.toList());
        return PageResponse.data(responses).pageInfo(page).ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:save:post')")
    @PostMapping(Endpoints.ROLE_SAVE)
    public Response<Void> save(@Valid @RequestBody RoleSaveRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        accountRoleService.save(request.getRoleCode(), request.getRoleName(), request.getNote(), account.getAccount(), request.getResourceIds());
        return Response.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:enable:post')")
    @PostMapping(Endpoints.ROLE_ENABLE)
    public Response<Void> enable(@Valid @RequestBody RoleEnableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.enable(request.getRoleCode(), account.getAccount());
        return Response.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:disable:post')")
    @PostMapping(Endpoints.ROLE_DISABLE)
    public Response<Void> disable(@Valid @RequestBody RoleDisableRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.disable(request.getRoleCode(), account.getAccount());
        return Response.ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:delete:post')")
    @PostMapping(Endpoints.ROLE_DELETE)
    public Response<Void> delete(@Valid @RequestBody RoleDeleteRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        accountRoleService.delete(request.getRoleCode(), account.getAccount());
        return Response.ok();
    }

    private RolePagesResponse toRolePagesResponse(BaseRoleBO entity) {
        RolePagesResponse response = new RolePagesResponse();
        response.setId(entity.getId());
        response.setNote(entity.getNote());
        response.setRoleCode(entity.getRoleCode());
        response.setRoleName(entity.getRoleName());
        response.setStatus(entity.getStatus());
        response.setCreateTime(entity.getCreateTime());
        response.setCreatePerson(entity.getCreatePerson());
        response.setUpdateTime(entity.getUpdateTime());
        response.setUpdatePerson(entity.getUpdatePerson());
        return response;
    }

}
