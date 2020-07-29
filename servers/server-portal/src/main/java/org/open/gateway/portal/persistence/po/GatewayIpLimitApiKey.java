package org.open.gateway.portal.persistence.po;

public class GatewayIpLimitApiKey {
    private Integer apiId;

    private Integer policyId;

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }
}