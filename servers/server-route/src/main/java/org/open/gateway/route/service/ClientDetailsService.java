package org.open.gateway.route.service;

import org.open.gateway.route.service.bo.ClientDetails;
import reactor.core.publisher.Mono;

/**
 * @author miko
 */
public interface ClientDetailsService {

    /**
     * 根据客户端id获取客户端信息(不为null)
     *
     * @param clientId 客户端id
     * @return 客户端信息(不为null)
     */
    Mono<ClientDetails> loadClientByClientId(String clientId);

}