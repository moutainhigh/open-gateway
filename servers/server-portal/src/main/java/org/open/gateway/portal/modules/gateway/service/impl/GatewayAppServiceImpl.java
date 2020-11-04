package org.open.gateway.portal.modules.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.OAuth2Constants;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.gateway.AuthorizedGrantTypeInvalidException;
import org.open.gateway.portal.exception.gateway.GatewayAppNotExistsException;
import org.open.gateway.portal.modules.gateway.service.GatewayAppService;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppBO;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppQuery;
import org.open.gateway.portal.persistence.mapper.GatewayAppApiMapperExt;
import org.open.gateway.portal.persistence.mapper.GatewayAppMapperExt;
import org.open.gateway.portal.persistence.mapper.OauthClientDetailsMapperExt;
import org.open.gateway.portal.persistence.po.GatewayApp;
import org.open.gateway.portal.persistence.po.GatewayAppApi;
import org.open.gateway.portal.persistence.po.OauthClientDetails;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by miko on 10/29/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class GatewayAppServiceImpl implements GatewayAppService {

    private final GatewayAppMapperExt gatewayAppMapper;
    private final GatewayAppApiMapperExt gatewayAppApiMapper;
    private final OauthClientDetailsMapperExt oauthClientDetailsMapper;

    @Override
    public List<GatewayAppBO> queryGatewayApps(GatewayAppQuery query) {
        List<GatewayApp> gatewayApps = gatewayAppMapper.selectByCondition(query);
        log.info("query gateway app num:{} param:{}", gatewayApps.size(), query);
        return gatewayApps.stream()
                .map(this::toGatewayAppBO)
                .collect(Collectors.toList());
    }

    @Override
    public GatewayAppBO queryGatewayAppByClientId(String clientId) throws GatewayAppNotExistsException {
        GatewayApp gatewayApp = gatewayAppMapper.selectByClientId(clientId);
        log.info("query gateway app client_id:{} result:{}", clientId, gatewayApp);
        if (gatewayApp == null) {
            throw new GatewayAppNotExistsException();
        }
        return toGatewayAppBO(gatewayApp);
    }

    @Transactional
    @Override
    public void save(String appCode, String appName, String note, String registerFrom, int accessTokenValidity, String webServerRedirectUri, Set<String> authorizedGrantTypes, Set<Integer> apiIds, String operator) throws AuthorizedGrantTypeInvalidException {
        // 校验支持的认证类型
        checkSupportedGrantTypes(authorizedGrantTypes);
        GatewayApp gatewayApp = gatewayAppMapper.selectByAppCode(appCode);
        if (gatewayApp == null) {
            log.info("app code:{} not exists. starting insert.", appCode);
            gatewayApp = new GatewayApp();
            gatewayApp.setAppCode(appCode);
            gatewayApp.setAppName(appName);
            gatewayApp.setClientId(BizUtil.generateClientId());
            gatewayApp.setClientSecret(BizUtil.generateClientSecret());
            gatewayApp.setRegisterFrom(registerFrom);
            gatewayApp.setStatus(BizConstants.STATUS.ENABLE);
            gatewayApp.setNote(note);
            gatewayApp.setCreateTime(new Date());
            gatewayApp.setCreatePerson(operator);
            BizUtil.checkUpdate(gatewayAppMapper.insertSelective(gatewayApp));
            log.info("insert app:{} finished. operator is:{} new id is:{}", appCode, operator, gatewayApp.getId());
            OauthClientDetails oauthClientDetails = new OauthClientDetails();
            oauthClientDetails.setClientId(gatewayApp.getClientId());
            oauthClientDetails.setClientSecret(gatewayApp.getClientSecret());
            oauthClientDetails.setAccessTokenValidity(accessTokenValidity);
            oauthClientDetails.setWebServerRedirectUri(webServerRedirectUri);
            oauthClientDetails.setAuthorizedGrantTypes(BizUtil.joinStringArray(authorizedGrantTypes));
            BizUtil.checkUpdate(oauthClientDetailsMapper.insertSelective(oauthClientDetails));
            log.info("insert oauth client details finished. client_id:{} operator:{}", gatewayApp.getClientId(), operator);
        } else {
            log.info("app code:{} exists. starting update id:{}.", appCode, gatewayApp.getId());
            gatewayApp.setAppName(appName);
            gatewayApp.setNote(note);
            gatewayApp.setUpdateTime(new Date());
            gatewayApp.setUpdatePerson(operator);
            BizUtil.checkUpdate(gatewayAppMapper.updateByPrimaryKeySelective(gatewayApp));
            log.info("update app:{} finished. operator is:{}", appCode, operator);
            OauthClientDetails oauthClientDetails = new OauthClientDetails();
            oauthClientDetails.setClientId(gatewayApp.getClientId());
            oauthClientDetails.setClientSecret(gatewayApp.getClientSecret());
            oauthClientDetails.setAccessTokenValidity(accessTokenValidity);
            oauthClientDetails.setWebServerRedirectUri(webServerRedirectUri);
            oauthClientDetails.setAuthorizedGrantTypes(BizUtil.joinStringArray(authorizedGrantTypes));
            BizUtil.checkUpdate(oauthClientDetailsMapper.updateByPrimaryKeySelective(oauthClientDetails));
            log.info("update oauth client details finished. client_id:{} operator:{}", gatewayApp.getClientId(), operator);
        }
        if (apiIds != null) {
            int appId = gatewayApp.getId();
            int count = gatewayAppApiMapper.deleteByAppId(appId);
            log.info("delete exists appApis by app id:{} delete count:{} operator:{}", appId, count, operator);
            if (!apiIds.isEmpty()) {
                BizUtil.checkUpdate(
                        gatewayAppApiMapper.insertBatch(
                                apiIds.stream()
                                        .map(apiId -> this.toGatewayAppApi(apiId, appId, operator))
                                        .collect(Collectors.toList())
                        ), apiIds.size()
                );
                log.info("insert batch appApis finished. operator is:{}", operator);
            }
        }
    }

    @Override
    public void enable(String appCode, String operator) throws GatewayAppNotExistsException {
        GatewayApp gatewayApp = queryExistsGatewayApp(appCode);
        GatewayApp param = new GatewayApp();
        param.setId(gatewayApp.getId());
        param.setStatus(BizConstants.STATUS.ENABLE);
        param.setUpdateTime(new Date());
        param.setUpdatePerson(operator);
        BizUtil.checkUpdate(gatewayAppMapper.updateByPrimaryKeySelective(param));
        log.info("enable gateway app by code:{} finished. operator is:{}", appCode, operator);
    }

    @Override
    public void disable(String appCode, String operator) throws GatewayAppNotExistsException {
        GatewayApp gatewayApp = queryExistsGatewayApp(appCode);
        GatewayApp param = new GatewayApp();
        param.setId(gatewayApp.getId());
        param.setStatus(BizConstants.STATUS.DISABLE);
        param.setUpdateTime(new Date());
        param.setUpdatePerson(operator);
        BizUtil.checkUpdate(gatewayAppMapper.updateByPrimaryKeySelective(param));
        log.info("disable gateway app by code:{} finished. operator is:{}", appCode, operator);
    }

    @Override
    public void delete(String appCode, String operator) throws GatewayAppNotExistsException {
        GatewayApp gatewayApp = queryExistsGatewayApp(appCode);
        BizUtil.checkUpdate(gatewayAppMapper.deleteByPrimaryKey(gatewayApp.getId()));
        log.info("delete gateway app by code:{} finished. id is:{} operator is:{}", appCode, gatewayApp.getId(), operator);
    }

    private GatewayApp queryExistsGatewayApp(String appCode) throws GatewayAppNotExistsException {
        GatewayApp gatewayApp = gatewayAppMapper.selectByAppCode(appCode);
        log.info("query gateway app by code:{} result:{}", appCode, gatewayApp);
        if (gatewayApp == null) {
            throw new GatewayAppNotExistsException();
        }
        return gatewayApp;
    }

    private void checkSupportedGrantTypes(Set<String> authorizedGrantTypes) throws AuthorizedGrantTypeInvalidException {
        if (authorizedGrantTypes != null) {
            // 只支持客户端模式
            if (authorizedGrantTypes.size() != 1 && !authorizedGrantTypes.contains(OAuth2Constants.GrantType.CLIENT_CREDENTIALS)) {
                log.warn("only support client_credentials grant type now.");
                throw new AuthorizedGrantTypeInvalidException();
            }
        }
    }

    private GatewayAppBO toGatewayAppBO(GatewayApp entity) {
        GatewayAppBO result = new GatewayAppBO();
        result.setId(entity.getId());
        result.setAppCode(entity.getAppCode());
        result.setAppName(entity.getAppName());
        result.setClientId(entity.getClientId());
        result.setClientSecret(entity.getClientSecret());
        result.setRegisterFrom(entity.getRegisterFrom());
        if (entity.getStatus() != null) {
            result.setStatus(Integer.valueOf(entity.getStatus()));
        }
        result.setNote(entity.getNote());
        result.setCreateTime(entity.getCreateTime());
        result.setCreatePerson(entity.getCreatePerson());
        result.setUpdateTime(entity.getUpdateTime());
        result.setUpdatePerson(entity.getUpdatePerson());
        return result;
    }

    private GatewayAppApi toGatewayAppApi(int apiId, int appId, String operator) {
        GatewayAppApi appApi = new GatewayAppApi();
        appApi.setCreateTime(new Date());
        appApi.setCreatePerson(operator);
        appApi.setAppId(appId);
        appApi.setApiId(apiId);
        return appApi;
    }

}
