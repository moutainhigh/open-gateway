package org.open.gateway.route.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 2020/7/17.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class ClientResource {

    private String clientId;

    private String routePath;

    private String apiPath;

}
