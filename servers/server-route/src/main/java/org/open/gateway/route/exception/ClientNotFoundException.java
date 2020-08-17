package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by miko on 2020/7/9.
 * 未找到客户端id
 *
 * @author MIKO
 */
public class ClientNotFoundException extends ResponseStatusException {

    public ClientNotFoundException() {
        super(HttpStatus.UNAUTHORIZED, "Client not found");
    }

}
