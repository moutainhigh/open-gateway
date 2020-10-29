package org.open.gateway.portal.modules.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.exception.gateway.GatewayAppNotExistsException;
import org.open.gateway.portal.modules.gateway.service.GatewayAppService;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppBO;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppQuery;
import org.open.gateway.portal.persistence.mapper.GatewayAppMapperExt;
import org.open.gateway.portal.persistence.po.GatewayApp;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<GatewayAppBO> queryGatewayApps(String clientId, String appName) {
        GatewayAppQuery query = new GatewayAppQuery();
        query.setClientId(clientId);
        query.setAppName(appName);
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

    private GatewayAppBO toGatewayAppBO(GatewayApp entity) {
        GatewayAppBO result = new GatewayAppBO();
        result.setId(entity.getId());
        result.setAppName(entity.getAppName());
        result.setClientId(entity.getClientId());
        result.setClientSecret(entity.getClientSecret());
        result.setRegisterFrom(entity.getRegisterFrom());
        result.setStatus(entity.getStatus());
        result.setNote(entity.getNote());
        result.setCreateTime(entity.getCreateTime());
        result.setCreatePerson(entity.getCreatePerson());
        result.setUpdateTime(entity.getUpdateTime());
        result.setUpdatePerson(entity.getUpdatePerson());
        return result;
    }

}
