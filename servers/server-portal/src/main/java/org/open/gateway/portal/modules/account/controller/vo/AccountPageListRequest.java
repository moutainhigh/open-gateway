package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.vo.PageRequest;

import java.util.Date;

/**
 * Created by miko on 10/26/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountPageListRequest extends PageRequest {

    private String account;

    private String phone;

    private String email;

    private Date createDateBegin;

    private Date createDateEnd;

    private Byte status;

}
