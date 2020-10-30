package org.open.gateway.portal.modules.gateway.controller;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.exception.gateway.AuthorizedGrantTypeInvalidException;
import org.open.gateway.portal.modules.gateway.controller.vo.GatewayAppPagesRequest;
import org.open.gateway.portal.modules.gateway.controller.vo.GatewayAppPagesResponse;
import org.open.gateway.portal.modules.gateway.controller.vo.GatewayAppSaveRequest;
import org.open.gateway.portal.modules.gateway.service.GatewayAppService;
import org.open.gateway.portal.modules.gateway.service.OauthClientDetailsService;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppBO;
import org.open.gateway.portal.modules.gateway.service.bo.OauthClientDetailsBO;
import org.open.gateway.portal.security.AccountDetails;
import org.open.gateway.portal.vo.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/23.
 *
 * @author MIKO
 */
@RestController
@AllArgsConstructor
public class GatewayAppController {

    private final GatewayAppService gatewayAppService;
    private final OauthClientDetailsService oauthClientDetailsService;

    @PreAuthorize("#account.hasPermission('gateway:app:pages:post')")
    @PostMapping(Endpoints.APP_PAGES)
    public Result pages(@Valid @RequestBody GatewayAppPagesRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) {
        Page<?> page = request.startPage();
        List<GatewayAppBO> apps = gatewayAppService.queryGatewayApps(request.getAppCode(), request.getAppName());
        LoadingCache<String, OauthClientDetailsBO> cache = Caffeine.newBuilder().build(oauthClientDetailsService::queryClientDetailsByClientId);
        List<GatewayAppPagesResponse> responses = apps.stream()
                .map(bo -> toGatewayAppPagesResponse(bo, cache))
                .collect(Collectors.toList());
        return Result.data(responses).pageInfo(page).ok();
    }

    @PreAuthorize("#account.hasPermission('gateway:app:save:post')")
    @PostMapping(Endpoints.APP_SAVE)
    public Result save(@Valid @RequestBody GatewayAppSaveRequest request, @AuthenticationPrincipal(errorOnInvalidType = true) AccountDetails account) throws AuthorizedGrantTypeInvalidException {
        gatewayAppService.save(request.getAppCode(), request.getAppName(), request.getNote(), request.getRegisterFrom(), request.getAccessTokenValidity(), request.getWebServerRedirectUri(), request.getAuthorizedGrantTypes(), account.getAccount());
        return Result.ok();
    }

    private GatewayAppPagesResponse toGatewayAppPagesResponse(GatewayAppBO gatewayApp, LoadingCache<String, OauthClientDetailsBO> cache) {
        GatewayAppPagesResponse response = new GatewayAppPagesResponse();
        OauthClientDetailsBO oauthClientDetails = cache.get(gatewayApp.getClientId());
        if (oauthClientDetails != null) {
            response.setWebServerRedirectUri(oauthClientDetails.getWebServerRedirectUri());
            response.setAuthorizedGrantTypes(oauthClientDetails.getAuthorizedGrantTypes());
            response.setScopes(oauthClientDetails.getScope());
        }
        response.setAppName(gatewayApp.getAppName());
        response.setId(gatewayApp.getId());
        response.setClientSecret(gatewayApp.getClientSecret());
        response.setClientId(gatewayApp.getClientId());
        response.setCreateTime(gatewayApp.getCreateTime());
        response.setCreatePerson(gatewayApp.getCreatePerson());
        response.setUpdateTime(gatewayApp.getUpdateTime());
        response.setUpdatePerson(gatewayApp.getUpdatePerson());
        response.setRegisterFrom(gatewayApp.getRegisterFrom());
        response.setStatus(gatewayApp.getStatus());
        response.setNote(gatewayApp.getNote());
        return response;
    }

}
