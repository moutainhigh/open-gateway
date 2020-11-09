package org.open.gateway.portal.modules.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.gateway.RouteTypeInvalidException;
import org.open.gateway.portal.modules.gateway.service.GatewayRouteService;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayRouteBO;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayRouteQuery;
import org.open.gateway.portal.persistence.mapper.GatewayRouteMapperExt;
import org.open.gateway.portal.persistence.po.GatewayRoute;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by miko on 11/9/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class GatewayRouteServiceImpl implements GatewayRouteService {

    private final GatewayRouteMapperExt gatewayRouteMapper;

    @Override
    public List<GatewayRouteBO> queryGatewayRoutes(GatewayRouteQuery query) {
        List<GatewayRoute> gatewayRoutes = gatewayRouteMapper.selectByCondition(query);
        log.info("query gateway routes param:{} result num:{}", query, gatewayRoutes.size());
        return gatewayRoutes.stream()
                .map(this::toGatewayRouteBO)
                .collect(Collectors.toList());
    }

    @Override
    public GatewayRouteBO queryGatewayRouteByRouteCode(String routeCode) {
        GatewayRoute gatewayRoute = gatewayRouteMapper.selectByRouteCode(routeCode);
        log.info("query gateway route by code:{} result:{}", routeCode, gatewayRoute);
        if (gatewayRoute == null) {
            return null;
        }
        return toGatewayRouteBO(gatewayRoute);
    }

    @Override
    public void save(String routeCode, String routeName, byte routeType, String routePath, String url, Integer stripPrefix, String note, String operator) throws RouteTypeInvalidException {
        GatewayRoute gatewayRoute = gatewayRouteMapper.selectByRouteCode(routeCode);
        if (gatewayRoute == null) {
            log.info("gateway route code:{} not exists. starting insert.", routeCode);
            // 校验路由类型
            checkRouteType(routeType);
            gatewayRoute = new GatewayRoute();
            gatewayRoute.setRouteCode(routeCode);
            gatewayRoute.setRouteName(routeName);
            gatewayRoute.setRoutePath(routePath);
            gatewayRoute.setUrl(url);
            gatewayRoute.setStripPrefix(Byte.valueOf(String.valueOf(Optional.ofNullable(stripPrefix).orElse(1))));
            gatewayRoute.setStatus(BizConstants.STATUS.ENABLE);
            gatewayRoute.setNote(note);
            gatewayRoute.setCreatePerson(operator);
            gatewayRoute.setCreateTime(new Date());
            BizUtil.checkUpdate(gatewayRouteMapper.insertSelective(gatewayRoute));
            log.info("insert gateway route:{} finished. operator is:{} new id is:{}", routeCode, operator, gatewayRoute.getId());
        } else {
            log.info("gateway route code:{} exists. starting update id:{}.", routeCode, gatewayRoute.getId());
            gatewayRoute.setRouteName(routeName);
            gatewayRoute.setRouteType(routeType);
            gatewayRoute.setUrl(url);
            gatewayRoute.setStripPrefix(Byte.valueOf(String.valueOf(Optional.ofNullable(stripPrefix).orElse(1))));
            gatewayRoute.setNote(note);
            gatewayRoute.setUpdatePerson(operator);
            gatewayRoute.setUpdateTime(new Date());
            BizUtil.checkUpdate(gatewayRouteMapper.updateByPrimaryKeySelective(gatewayRoute));
            log.info("update gateway route:{} finished. operator is:{}", routeCode, operator);
        }
    }

    @Override
    public void enable(String routeCode, String operator) {
        // TODO
    }

    @Override
    public void disable(String routeCode, String operator) {
        // TODO
    }

    @Override
    public void delete(String routeCode, String operator) {
        // TODO
    }

    private void checkRouteType(byte routeType) throws RouteTypeInvalidException {
        if (routeType != BizConstants.ROUTE_TYPE.HTTP && routeType != BizConstants.ROUTE_TYPE.DUBBO) {
            throw new RouteTypeInvalidException();
        }
    }

    private GatewayRouteBO toGatewayRouteBO(GatewayRoute entity) {
        GatewayRouteBO result = new GatewayRouteBO();
        result.setId(entity.getId());
        result.setRouteCode(entity.getRouteCode());
        result.setRouteName(entity.getRouteName());
        result.setRouteType(entity.getRouteType());
        result.setRoutePath(entity.getRoutePath());
        result.setUrl(entity.getUrl());
        if (entity.getStatus() != null) {
            result.setStatus(Integer.valueOf(entity.getStatus()));
        }
        result.setStripPrefix(entity.getStripPrefix());
        result.setNote(entity.getNote());
        result.setCreateTime(entity.getCreateTime());
        result.setCreatePerson(entity.getCreatePerson());
        result.setUpdateTime(entity.getUpdateTime());
        result.setUpdatePerson(entity.getUpdatePerson());
        return result;
    }

}
