package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * Created by miko on 10/26/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountRoleSaveRequest {

    @NotBlank
    private String roleCode;

    private String roleName;

    private String note;

}
