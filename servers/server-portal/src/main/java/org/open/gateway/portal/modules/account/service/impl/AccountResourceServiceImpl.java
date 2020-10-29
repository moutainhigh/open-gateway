package org.open.gateway.portal.modules.account.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.account.ResourceNotExistsException;
import org.open.gateway.portal.modules.account.service.AccountResourceService;
import org.open.gateway.portal.modules.account.service.bo.BaseResourceBO;
import org.open.gateway.portal.persistence.mapper.BaseResourceMapperExt;
import org.open.gateway.portal.persistence.po.BaseResource;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by miko on 10/22/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class AccountResourceServiceImpl implements AccountResourceService {

    private final BaseResourceMapperExt baseResourceMapper;

    @Override
    public List<BaseResourceBO> queryResources() {
        List<BaseResource> resources = baseResourceMapper.selectAll();
        log.info("query all resources num:{}", resources.size());
        // 转换结构
        return resources.stream()
                .map(this::toBaseResourceBO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseResourceBO> queryResourcesByAccount(String account) {
        // 查询资源
        List<BaseResource> resources = baseResourceMapper.selectByAccountAndResourceType(account, null);
        log.info("query resources num:{} by account:{}", resources.size(), account);
        // 转换结构
        return resources.stream()
                .map(this::toBaseResourceBO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseResourceBO> queryResourcesByRole(String roleCode) {
        // 查询资源
        List<BaseResource> resources = baseResourceMapper.selectByRole(roleCode);
        log.info("query resources num:{} by role:{}", resources.size(), roleCode);
        // 转换结构
        return resources.stream()
                .map(this::toBaseResourceBO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> queryPermsByAccount(String account) {
        List<BaseResource> baseResources = baseResourceMapper.selectByAccountAndResourceType(account, BizConstants.RESOURCE_TYPE.BUTTON);
        log.info("query perms num:{}", baseResources.size());
        return baseResources.stream()
                .map(BaseResource::getPerms)
                .collect(Collectors.toSet());
    }

    @Override
    public void save(String resourceCode, String resourceName, String resourceType, String parentCode, String perms, String url, Integer sort, String note, String operator) throws ResourceNotExistsException {
        // 校验资源类型
        checkResourceType(resourceType, url, perms);
        // 校验上级代码
        checkParentCode(parentCode);
        BaseResource resource = baseResourceMapper.selectByCode(resourceCode);
        if (resource == null) {
            log.info("resource code:{} not exists. starting insert.", resourceCode);
            resource = new BaseResource();
            resource.setResourceCode(resourceCode);
            resource.setResourceName(resourceName);
            resource.setParentCode(Optional.ofNullable(parentCode).orElse(BizConstants.RESOURCE_ROOT_CODE));
            resource.setUrl(url);
            resource.setResourceType(resourceType);
            resource.setPerms(perms);
            resource.setNote(note);
            resource.setSort(Optional.ofNullable(sort).orElse(BizConstants.DEFAULT_SORT));
            resource.setStatus(BizConstants.STATUS.ENABLE);
            resource.setCreateTime(new Date());
            resource.setCreatePerson(operator);
            resource.setIsDel(BizConstants.DEL_FLAG.NO);
            BizUtil.checkUpdate(baseResourceMapper.insertSelective(resource));
            log.info("insert resource finished. operator:{} new resource is:{}", operator, resource.getId());
        } else {
            log.info("resource code:{} exists. starting update id:{}.", resourceCode, resource.getId());
            resource.setResourceCode(resourceCode);
            resource.setResourceName(resourceName);
            resource.setParentCode(parentCode);
            resource.setUrl(url);
            resource.setResourceType(resourceType);
            resource.setPerms(perms);
            resource.setNote(note);
            resource.setSort(sort);
            resource.setUpdateTime(new Date());
            resource.setUpdatePerson(operator);
            BizUtil.checkUpdate(baseResourceMapper.updateByPrimaryKeySelective(resource));
            log.info("update resource finished. operator:{}", operator);
        }
    }

    @Override
    public void delete(String resourceCode, String operator) throws ResourceNotExistsException {
        BaseResource resource = baseResourceMapper.selectByCode(resourceCode);
        if (resource == null) {
            throw new ResourceNotExistsException();
        }
        BaseResource param = new BaseResource();
        param.setId(resource.getId());
        param.setIsDel(BizConstants.DEL_FLAG.YES);
        BizUtil.checkUpdate(baseResourceMapper.updateByPrimaryKeySelective(param));
        log.info("logic delete resource:{} finished. operator is:{}", resourceCode, operator);
    }

    private BaseResourceBO toBaseResourceBO(BaseResource br) {
        BaseResourceBO ar = new BaseResourceBO();
        ar.setId(br.getId());
        ar.setResourceCode(br.getResourceCode());
        ar.setResourceName(br.getResourceName());
        ar.setResourceType(br.getResourceType());
        ar.setParentCode(br.getParentCode());
        ar.setPerms(br.getPerms());
        ar.setUrl(br.getUrl());
        ar.setSort(br.getSort());
        ar.setNote(br.getNote());
        return ar;
    }

    private void checkResourceType(String resourceType, String url, String perms) {
        log.info("request resource type is:{}", resourceType);
        switch (resourceType) {
            case BizConstants.RESOURCE_TYPE.DIRECTORY:
                break;
            case BizConstants.RESOURCE_TYPE.MENU:
                if (url == null) {
                    throw new IllegalArgumentException("resource url is required");
                }
                break;
            case BizConstants.RESOURCE_TYPE.BUTTON:
                if (perms == null) {
                    throw new IllegalArgumentException("resource perms is required");
                }
                break;
            default:
                throw new IllegalArgumentException("invalid resource type");
        }
    }

    private void checkParentCode(String parentCode) throws ResourceNotExistsException {
        if (StringUtil.isNotBlank(parentCode)) {
            BaseResource baseResource = baseResourceMapper.selectByCode(parentCode);
            if (baseResource == null) {
                log.info("parent code:{} not exists.", parentCode);
                throw new ResourceNotExistsException();
            }
        }
    }

}
