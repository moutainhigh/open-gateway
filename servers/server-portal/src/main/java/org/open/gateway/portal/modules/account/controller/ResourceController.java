package org.open.gateway.portal.modules.account.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.exception.account.ResourceNotExistsException;
import org.open.gateway.portal.exception.account.RoleNotExistsException;
import org.open.gateway.portal.modules.account.controller.vo.ResourceDeleteRequest;
import org.open.gateway.portal.modules.account.controller.vo.ResourceSaveRequest;
import org.open.gateway.portal.modules.account.controller.vo.ResourceTreeResponse;
import org.open.gateway.portal.modules.account.controller.vo.RoleResourcesRequest;
import org.open.gateway.portal.modules.account.service.AccountResourceService;
import org.open.gateway.portal.modules.account.service.bo.BaseResourceBO;
import org.open.gateway.portal.security.AccountDetails;
import org.open.gateway.portal.vo.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by miko on 10/28/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@RestController
public class ResourceController {

    private final AccountResourceService accountResourceService;

    @PostMapping(Endpoints.ACCOUNT_RESOURCES)
    public Response<List<ResourceTreeResponse>> accountResources(@AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        List<BaseResourceBO> resources = accountResourceService.queryResourcesByAccount(account.getAccount());
        List<ResourceTreeResponse> responses = toResourceTree(resources);
        return Response.data(responses).ok();
    }

    @PreAuthorize("#account.hasPermission('account:role:resources:post')")
    @PostMapping(Endpoints.ROLE_RESOURCES)
    public Response<List<ResourceTreeResponse>> roleResources(@Valid @RequestBody RoleResourcesRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws RoleNotExistsException {
        List<BaseResourceBO> resources = accountResourceService.queryResourcesByRole(request.getRoleCode());
        List<ResourceTreeResponse> responses = toResourceTree(resources);
        return Response.data(responses).ok();
    }

    @PreAuthorize("#account.hasPermission('account:resource:list:post')")
    @PostMapping(Endpoints.RESOURCE_LIST)
    public Response<List<ResourceTreeResponse>> resourceList(@AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        List<BaseResourceBO> resources = accountResourceService.queryResources();
        List<ResourceTreeResponse> responses = toResourceTree(resources);
        return Response.data(responses).ok();
    }

    @PreAuthorize("#account.hasPermission('account:resource:save:post')")
    @PostMapping(Endpoints.RESOURCE_SAVE)
    public Response<Void> save(@Valid @RequestBody ResourceSaveRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws ResourceNotExistsException {
        accountResourceService.save(request.getResourceCode(), request.getResourceName(), request.getResourceType(), request.getParentCode(), request.getPerms(), request.getUrl(), request.getSort(), request.getNote(), account.getAccount());
        return Response.ok();
    }

    @PreAuthorize("#account.hasPermission('account:resource:delete:post')")
    @PostMapping(Endpoints.RESOURCE_DELETE)
    public Response<Void> delete(@Valid @RequestBody ResourceDeleteRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws ResourceNotExistsException {
        accountResourceService.delete(request.getResourceCode(), account.getAccount());
        return Response.ok();
    }

    private List<ResourceTreeResponse> toResourceTree(List<BaseResourceBO> resources) {
        // 按照父代码分组
        Map<String, List<ResourceTreeResponse>> resourcesGroup = resources.stream()
                .map(this::toResourceTreeResponse)
                .collect(Collectors.groupingBy(ResourceTreeResponse::getParentCode));

        // 获取子节点
        List<ResourceTreeResponse> rootResources = getResourceChildren(BizConstants.RESOURCE_ROOT_CODE, resourcesGroup);
        log.info("root resources num:{}", rootResources.size());
        return rootResources;
    }

    private List<ResourceTreeResponse> getResourceChildren(String parentCode, Map<String, List<ResourceTreeResponse>> resourcesGroup) {
        List<ResourceTreeResponse> resources = resourcesGroup.getOrDefault(parentCode, new ArrayList<>());
        Collections.sort(resources);
        // 按照sort排序
        for (ResourceTreeResponse r : resources) {
            r.setChildren(getResourceChildren(r.getResourceCode(), resourcesGroup));
        }
        return resources;
    }

    private ResourceTreeResponse toResourceTreeResponse(BaseResourceBO resource) {
        ResourceTreeResponse response = new ResourceTreeResponse();
        response.setParentCode(resource.getParentCode());
        response.setPerms(resource.getPerms());
        response.setUrl(resource.getUrl());
        response.setSort(resource.getSort());
        response.setNote(resource.getNote());
        response.setResourceCode(resource.getResourceCode());
        response.setResourceName(resource.getResourceName());
        response.setResourceType(resource.getResourceType());
        response.setId(resource.getId());
        return response;
    }

}
