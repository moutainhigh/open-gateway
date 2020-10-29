package org.open.gateway.portal.modules.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.modules.gateway.service.OauthClientDetailsService;
import org.open.gateway.portal.modules.gateway.service.bo.OauthClientDetailsBO;
import org.open.gateway.portal.persistence.mapper.OauthClientDetailsMapperExt;
import org.open.gateway.portal.persistence.po.OauthClientDetails;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;

/**
 * Created by miko on 10/29/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {

    private final OauthClientDetailsMapperExt oauthClientDetailsMapper;

    @Override
    public OauthClientDetailsBO queryClientDetailsByClientId(String clientId) {
        OauthClientDetails clientDetails = oauthClientDetailsMapper.selectByPrimaryKey(clientId);
        log.info("query oauth client details client_id:{} result:{}", clientId, clientDetails);
        if (clientDetails == null) {
            return null;
        }
        return toOauthClientDetailsBO(clientDetails);
    }

    private OauthClientDetailsBO toOauthClientDetailsBO(OauthClientDetails entity) {
        OauthClientDetailsBO response = new OauthClientDetailsBO();
        response.setClientId(entity.getClientId());
        response.setClientSecret(entity.getClientSecret());
        response.setResourceIds(entity.getResourceIds());
        if (entity.getScope() != null) {
            response.setScope(BizUtil.splitArrayString(entity.getScope()));
        }
        if (entity.getAuthorizedGrantTypes() != null) {
            response.setAuthorizedGrantTypes(BizUtil.splitArrayString(entity.getAuthorizedGrantTypes()));
        }
        if (entity.getAuthorities() != null) {
            response.setAuthorities(BizUtil.splitArrayString(entity.getAuthorities()));
        }
        response.setWebServerRedirectUri(entity.getWebServerRedirectUri());
        response.setAccessTokenValidity(entity.getAccessTokenValidity());
        response.setRefreshTokenValidity(entity.getRefreshTokenValidity());
        response.setAdditionalInformation(entity.getAdditionalInformation());
        response.setAutoapprove(entity.getAutoapprove());
        return response;
    }

}
