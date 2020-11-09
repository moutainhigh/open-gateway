package org.open.gateway.portal.modules.gateway.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miko on 11/9/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayRouteBO {

    private Integer id;

    private String routeCode;

    private String routeName;

    private Byte routeType;

    private String routePath;

    private String url;

    private Byte stripPrefix;

    private Integer status;

    private String note;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;

}
