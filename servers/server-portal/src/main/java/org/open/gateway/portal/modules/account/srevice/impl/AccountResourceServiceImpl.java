package org.open.gateway.portal.modules.account.srevice.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.modules.account.srevice.AccountResourceService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountResourceBO;
import org.open.gateway.portal.persistence.mapper.BaseResourceMapperExt;
import org.open.gateway.portal.persistence.po.BaseResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public List<BaseAccountResourceBO> queryResourcesByAccount(String account) {
        // 查询所有资源
        List<BaseResource> resources = baseResourceMapper.selectResourcesByAccount(account);
        log.info("account:{} resources num:{}", account, resources.size());
        // 按照父代码分组
        Map<String, List<BaseAccountResourceBO>> resourcesGroup = resources.stream()
                .map(this::toAccountResourceBO)
                .collect(Collectors.groupingBy(BaseAccountResourceBO::getParentCode));
        // 根节点
        List<BaseAccountResourceBO> rootResources = toResourceTree(BizConstants.ROOT_CODE, resourcesGroup);
        log.info("root resources num:{}", rootResources.size());
        return rootResources;
    }

    private BaseAccountResourceBO toAccountResourceBO(BaseResource br) {
        BaseAccountResourceBO ar = new BaseAccountResourceBO();
        ar.setResourceCode(br.getResourceCode());
        ar.setResourceName(br.getResourceName());
        ar.setParentCode(br.getParentCode());
        ar.setUrl(br.getUrl());
        ar.setSort(br.getSort());
        ar.setNote(br.getNote());
        return ar;
    }

    private List<BaseAccountResourceBO> toResourceTree(String parentCode, Map<String, List<BaseAccountResourceBO>> resourcesGroup) {
        List<BaseAccountResourceBO> resources = resourcesGroup.getOrDefault(parentCode, new ArrayList<>());
        if (resources != null) {
            for (BaseAccountResourceBO r : resources) {
                r.setChildren(toResourceTree(r.getResourceCode(), resourcesGroup));
            }
        }
        return resources;
    }

}
