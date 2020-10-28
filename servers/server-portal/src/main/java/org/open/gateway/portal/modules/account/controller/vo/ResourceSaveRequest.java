package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.validate.annotation.In;

import javax.validation.constraints.NotBlank;

/**
 * Created by miko on 10/28/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class ResourceSaveRequest {

    @NotBlank
    private String resourceCode;

    @NotBlank
    private String resourceName;

    @In(range = {BizConstants.RESOURCE_TYPE.DIRECTORY, BizConstants.RESOURCE_TYPE.MENU, BizConstants.RESOURCE_TYPE.BUTTON})
    private String resourceType;

    private String parentCode;

    private String perms;

    private String url;

    private Integer sort;

    private String note;

}
