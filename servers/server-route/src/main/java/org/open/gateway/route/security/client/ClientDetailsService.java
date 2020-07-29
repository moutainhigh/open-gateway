package org.open.gateway.route.security.client;

import reactor.core.publisher.Mono;

/**
 * A service that provides the details about an OAuth2 client.
 *
 * @author Ryan Heaton
 */
public interface ClientDetailsService {

    /**
     * Load a client by the client id. This method must not return null.
     *
     * @param clientId The client id.
     * @return The client details (never null).
     */
    Mono<ClientDetails> loadClientByClientId(String clientId);

}