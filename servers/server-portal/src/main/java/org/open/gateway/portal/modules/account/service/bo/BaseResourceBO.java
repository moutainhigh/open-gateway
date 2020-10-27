package org.open.gateway.portal.modules.account.service.bo;

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
public class BaseResourceBO {

    private String resourceCode;

    private String resourceName;

    private String resourceType;

    private String parentCode;

    private String perms;

    private String url;

    private Integer sort;

    private String note;

    private List<BaseResourceBO> children;

}
