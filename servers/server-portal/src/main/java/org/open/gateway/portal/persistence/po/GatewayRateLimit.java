package org.open.gateway.portal.persistence.po;

import java.util.Date;

public class GatewayRateLimit {
    private Integer id;

    private String policyName;

    private String policyType;

    private Long limitQuota;

    private Long maxLimitQuota;

    private String intervalUnit;

    private Byte status;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;

    private Byte isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName == null ? null : policyName.trim();
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType == null ? null : policyType.trim();
    }

    public Long getLimitQuota() {
        return limitQuota;
    }

    public void setLimitQuota(Long limitQuota) {
        this.limitQuota = limitQuota;
    }

    public Long getMaxLimitQuota() {
        return maxLimitQuota;
    }

    public void setMaxLimitQuota(Long maxLimitQuota) {
        this.maxLimitQuota = maxLimitQuota;
    }

    public String getIntervalUnit() {
        return intervalUnit;
    }

    public void setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit == null ? null : intervalUnit.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson == null ? null : updatePerson.trim();
    }

    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }
}