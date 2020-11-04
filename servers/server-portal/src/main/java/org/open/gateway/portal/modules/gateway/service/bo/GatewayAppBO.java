package org.open.gateway.portal.modules.gateway.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miko on 10/29/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayAppBO {

    private Integer id;

    private String appCode;

    private String appName;

    private String clientId;

    private String clientSecret;

    private String registerFrom;

    private Integer status;

    private String note;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;

}
