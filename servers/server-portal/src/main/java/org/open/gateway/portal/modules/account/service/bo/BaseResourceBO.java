package org.open.gateway.portal.modules.account.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 9/27/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class BaseResourceBO {

    /**
     * id
     */
    private Integer id;

    /**
     * 资源代码
     */
    private String resourceCode;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 父资源代码
     */
    private String parentCode;

    /**
     * 权限
     */
    private String perms;

    /**
     * 地址
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String note;

}
