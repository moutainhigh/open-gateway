package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by miko on 10/22/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountUpdateRequest {

    /**
     * 账户
     */
    @NotBlank
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号码
     */
    @Pattern(regexp = "[0-9]{11}")
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String note;

    /**
     * 角色id集合
     */
    private List<Integer> roleIds;

}
