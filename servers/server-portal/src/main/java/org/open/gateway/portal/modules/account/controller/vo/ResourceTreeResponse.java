package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by miko on 9/27/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class ResourceTreeResponse {

    private Integer id;

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String parentCode;

    private String perms;

    private String url;

    private Integer sort;

    private String note;

    private List<ResourceTreeResponse> children;

}
