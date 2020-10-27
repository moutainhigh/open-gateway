package org.open.gateway.portal.modules.account.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.modules.account.service.AccountResourceService;
import org.open.gateway.portal.modules.account.service.bo.BaseResourceBO;
import org.open.gateway.portal.persistence.mapper.BaseResourceMapperExt;
import org.open.gateway.portal.persistence.po.BaseResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public List<BaseResourceBO> queryResourcesByAccount(String account) {
        // 查询资源
        List<BaseResource> resources = baseResourceMapper.selectResourcesByAccountAndResourceType(account, null);
        log.info("query resources num:{} by account:{}", resources.size(), account);
        // 按照父代码分组
        Map<String, List<BaseResourceBO>> resourcesGroup = resources.stream()
                .map(this::toBaseResourceBO)
                .collect(Collectors.groupingBy(BaseResourceBO::getParentCode));
        // 根节点
        List<BaseResourceBO> rootResources = toResourceTree(BizConstants.ROOT_CODE, resourcesGroup);
        log.info("root resources num:{}", rootResources.size());
        return rootResources;
    }

    @Override
    public List<BaseResourceBO> queryResourcesByRole(String roleCode) {
        // 查询资源
        List<BaseResource> resources = baseResourceMapper.selectResourcesByRole(roleCode);
        log.info("query resources num:{} by role:{}", resources.size(), roleCode);
        return resources.stream()
                .map(this::toBaseResourceBO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> queryPermsByAccount(String account) {
        List<BaseResource> baseResources = baseResourceMapper.selectResourcesByAccountAndResourceType(account, BizConstants.RESOURCE_TYPE.BUTTON);
        log.info("query perms num:{}", baseResources.size());
        return baseResources.stream()
                .map(BaseResource::getPerms)
                .collect(Collectors.toSet());
    }

    private BaseResourceBO toBaseResourceBO(BaseResource br) {
        BaseResourceBO ar = new BaseResourceBO();
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

    private List<BaseResourceBO> toResourceTree(String parentCode, Map<String, List<BaseResourceBO>> resourcesGroup) {
        List<BaseResourceBO> resources = resourcesGroup.getOrDefault(parentCode, new ArrayList<>());
        if (resources != null) {
            for (BaseResourceBO r : resources) {
                r.setChildren(toResourceTree(r.getResourceCode(), resourcesGroup));
            }
        }
        return resources;
    }

}
