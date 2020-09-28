package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountRegisterResponse {

    private Integer id;

    private String account;

    private String password;

    private String registerIp;

    private Byte status;

    private String phone;

    private String email;

    private String note;

}
