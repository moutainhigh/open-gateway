package org.open.gateway.portal.persistence.po;

import java.util.Date;

public class GatewayIpLimitApi extends GatewayIpLimitApiKey {
    private Date createTime;

    private String createPerson;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }
}