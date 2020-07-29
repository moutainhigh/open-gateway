package org.open.gateway.portal.persistence.po;

import java.util.Date;

public class GatewayRoute {
    private Integer id;

    private String routeCode;

    private String routeName;

    private Byte routeType;

    private String routePath;

    private String url;

    private Byte stripPrefix;

    private Byte retryable;

    private Byte status;

    private String desc;

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

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode == null ? null : routeCode.trim();
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName == null ? null : routeName.trim();
    }

    public Byte getRouteType() {
        return routeType;
    }

    public void setRouteType(Byte routeType) {
        this.routeType = routeType;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath == null ? null : routePath.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Byte getStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(Byte stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public Byte getRetryable() {
        return retryable;
    }

    public void setRetryable(Byte retryable) {
        this.retryable = retryable;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
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