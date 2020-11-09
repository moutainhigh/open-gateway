package org.open.gateway.portal.modules.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.OAuth2Constants;
import org.open.gateway.common.utils.CollectionUtil;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.gateway.AuthorizedGrantTypeInvalidException;
import org.open.gateway.portal.exception.gateway.GatewayAppNotExistsException;
import org.open.gateway.portal.modules.gateway.service.GatewayAppService;
import org.open.gateway.portal.modules.gateway.service.GatewayService;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppBO;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppQuery;
import org.open.gateway.portal.persistence.mapper.GatewayApiMapperExt;
import org.open.gateway.portal.persistence.mapper.GatewayAppMapperExt;
import org.open.gateway.portal.persistence.mapper.GatewayGroupMapperExt;
import org.open.gateway.portal.persistence.mapper.OauthClientDetailsMapperExt;
import org.open.gateway.portal.persistence.po.*;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private final GatewayApiMapperExt gatewayApiMapper;
    private final GatewayGroupMapperExt gatewayGroupMapper;
    private final OauthClientDetailsMapperExt oauthClientDetailsMapper;
    private final GatewayService gatewayService;

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
    public void save(String appCode, String appName, String note,
                     String registerFrom, int accessTokenValidity, String webServerRedirectUri,
                     Set<String> authorizedGrantTypes, Set<Integer> groupIds, Set<Integer> apiIds,
                     String operator) throws AuthorizedGrantTypeInvalidException {
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
            OauthClientDetails oauthClientDetails = getOauthClientDetails(accessTokenValidity, webServerRedirectUri, authorizedGrantTypes, groupIds, apiIds, gatewayApp);
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
            OauthClientDetails oauthClientDetails = getOauthClientDetails(accessTokenValidity, webServerRedirectUri, authorizedGrantTypes, groupIds, apiIds, gatewayApp);
            BizUtil.checkUpdate(oauthClientDetailsMapper.updateByPrimaryKeySelective(oauthClientDetails));
            log.info("update oauth client details finished. client_id:{} operator:{}", gatewayApp.getClientId(), operator);
            // 刷新app token
            gatewayService.refreshClientToken(CollectionUtil.newHashSet(gatewayApp.getClientId()));
        }
    }

    /**
     * 获取客户端明细
     *
     * @param accessTokenValidity  token有效时间
     * @param webServerRedirectUri 重定向地址
     * @param authorizedGrantTypes 授权类型
     * @param groupIds             分组id集合
     * @param apiIds               接口id集合
     * @param gatewayApp           网关应用
     * @return 客户端明细
     */
    private OauthClientDetails getOauthClientDetails(int accessTokenValidity, String webServerRedirectUri, Set<String> authorizedGrantTypes,
                                                     Set<Integer> groupIds, Set<Integer> apiIds,
                                                     GatewayApp gatewayApp) {
        OauthClientDetails oauthClientDetails = new OauthClientDetails();
        oauthClientDetails.setClientId(gatewayApp.getClientId());
        oauthClientDetails.setClientSecret(gatewayApp.getClientSecret());
        oauthClientDetails.setAccessTokenValidity(accessTokenValidity);
        oauthClientDetails.setWebServerRedirectUri(webServerRedirectUri);
        oauthClientDetails.setAuthorizedGrantTypes(BizUtil.joinStringArray(authorizedGrantTypes));
        if (groupIds != null) {
            List<String> groupCodes = groupIds.stream()
                    .map(gatewayGroupMapper::selectByPrimaryKey)
                    .filter(Objects::nonNull)
                    .map(GatewayGroup::getGroupCode)
                    .collect(Collectors.toList());
            log.info("group codes:{} num:{} param num:{}", groupCodes, groupCodes.size(), groupIds.size());
            oauthClientDetails.setScope(BizUtil.joinStringArray(groupCodes));
        }
        if (apiIds != null) {
            List<String> apiCodes = apiIds.stream()
                    .map(gatewayApiMapper::selectByPrimaryKey)
                    .filter(Objects::nonNull)
                    .map(GatewayApi::getApiCode)
                    .collect(Collectors.toList());
            log.info("api codes:{} num:{} param num:{}", apiCodes, apiCodes.size(), apiIds.size());
            oauthClientDetails.setAuthorities(BizUtil.joinStringArray(apiCodes));
        }
        return oauthClientDetails;
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
