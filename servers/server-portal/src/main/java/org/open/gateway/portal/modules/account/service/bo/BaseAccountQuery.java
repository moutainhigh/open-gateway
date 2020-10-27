package org.open.gateway.portal.modules.account.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class BaseAccountQuery {

    private String account;

    private String registerIp;

    private Byte status;

    private String phone;

    private String email;

    private Date createTimeBegin;

    private Date createTimeEnd;

    private String createPerson;

}