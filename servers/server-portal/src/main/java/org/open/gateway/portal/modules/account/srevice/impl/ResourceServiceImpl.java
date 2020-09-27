package org.open.gateway.portal.modules.account.srevice.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.modules.account.srevice.ResourceService;
import org.open.gateway.portal.modules.account.srevice.bo.AccountResourceBO;
import org.open.gateway.portal.persistence.mapper.BaseResourceMapperExt;
import org.open.gateway.portal.persistence.po.BaseResource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miko on 9/27/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class ResourceServiceImpl implements ResourceService {

    private final BaseResourceMapperExt baseResourceMapper;

    @Override
    public AccountResourceBO queryResourcesByAccount(String account) {
        // TODO
        List<BaseResource> resources = baseResourceMapper.selectByAccount(account);

        return null;
    }

}
