package org.open.gateway.portal.modules.account.srevice.bo;

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
public class AccountResourceBO {

    private String resourceCode;

    private String resourceName;

    private String parentCode;

    private String url;

    private Integer sort;

    private String note;

    private List<AccountResourceBO> children;

}
