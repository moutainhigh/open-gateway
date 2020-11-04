package org.open.gateway.portal.modules.account.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miko on 10/27/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class BaseRoleBO {

    private Integer id;

    private String roleCode;

    private String roleName;

    private String note;

    private Integer status;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;

}
