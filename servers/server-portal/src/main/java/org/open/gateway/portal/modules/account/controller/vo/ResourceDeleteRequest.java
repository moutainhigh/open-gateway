package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * Created by miko on 10/28/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class ResourceDeleteRequest {

    @NotBlank
    private String resourceCode;

}
