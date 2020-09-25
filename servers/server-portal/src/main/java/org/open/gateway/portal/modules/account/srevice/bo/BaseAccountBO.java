package org.open.gateway.portal.modules.account.srevice.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class BaseAccountBO {

    private Integer id;

    private String account;

    private String password;

    private String salt;

    private String registerIp;

    private Byte status;

    private String phone;

    private String email;

    private String note;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;

    private Byte isDel;

}
