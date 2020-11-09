package org.open.gateway.portal.modules.gateway.service;

import org.open.gateway.portal.exception.gateway.RouteTypeInvalidException;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayRouteBO;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayRouteQuery;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Created by miko on 11/9/20.
 *
 * @author MIKO
 */
public interface GatewayRouteService {

    /**
     * 查询网关路由列表
     *
     * @param query 查询参数
     * @return 路由列表
     */
    @NonNull
    List<GatewayRouteBO> queryGatewayRoutes(GatewayRouteQuery query);

    /**
     * 根据路由代码查询路由信息
     *
     * @param routeCode 路由代码
     * @return 路由信息
     */
    @Nullable
    GatewayRouteBO queryGatewayRouteByRouteCode(String routeCode);

    /**
     * 保存网关路由
     *
     * @param routeCode   路由代码
     * @param routeName   路由名称
     * @param routeType   路由类型
     * @param routePath   路由地址
     * @param url         代理地址
     * @param stripPrefix 忽略前缀数量
     * @param note        备注
     * @param operator    操作人
     */
    void save(String routeCode, String routeName, byte routeType, String routePath, String url, Integer stripPrefix, String note, String operator) throws RouteTypeInvalidException;

    /**
     * 启用网关路由
     *
     * @param routeCode 路由代码
     * @param operator  操作人
     */
    void enable(String routeCode, String operator);

    /**
     * 禁用网关路由
     *
     * @param routeCode 路由代码
     * @param operator  操作人
     */
    void disable(String routeCode, String operator);

    /**
     * 删除网关路由
     *
     * @param routeCode 路由代码
     * @param operator  操作人
     */
    void delete(String routeCode, String operator);

}
