package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by miko on 10/26/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class RoleSaveRequest {

    @NotBlank
    private String roleCode;

    @NotBlank
    private String roleName;

    private String note;

    /**
     * 资源id集合
     */
    private List<Integer> resourceIds;

}
