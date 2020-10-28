package org.open.gateway.portal.modules.account.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.role.RoleNotExistsException;
import org.open.gateway.portal.modules.account.service.AccountRoleService;
import org.open.gateway.portal.modules.account.service.bo.BaseRoleBO;
import org.open.gateway.portal.persistence.mapper.BaseRoleMapperExt;
import org.open.gateway.portal.persistence.po.BaseRole;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by miko on 10/27/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class AccountRoleServiceImpl implements AccountRoleService {

    private final BaseRoleMapperExt baseRoleMapper;

    @Override
    public List<BaseRoleBO> queryRolesByAccount(String account) {
        List<BaseRole> baseRoles = baseRoleMapper.selectRolesByAccount(account);
        log.info("query roles num:{} by account:{}", baseRoles.size(), account);
        return baseRoles.stream()
                .map(this::toBaseRoleBO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveRole(String roleCode, String roleName, String note, String operator, List<Integer> resourceIds) {
        BaseRole param = baseRoleMapper.selectRoleByCode(roleCode);
        if (param == null) {
            log.info("role code:{} not exists. starting insert.", roleCode);
            param = new BaseRole();
            param.setRoleCode(roleCode);
            param.setRoleName(roleName);
            param.setNote(note);
            param.setCreateTime(new Date());
            param.setCreatePerson(operator);
            param.setIsDel(BizConstants.DEL_FLAG.NO);
            BizUtil.checkUpdate(baseRoleMapper.insertSelective(param));
            log.info("insert role:{} finished. operator is:{} new id is:{}", roleCode, operator, param.getId());
        } else {
            log.info("role code:{} exists. starting update.", roleCode);
            param.setRoleName(roleName);
            param.setNote(note);
            param.setUpdateTime(new Date());
            param.setUpdatePerson(operator);
            BizUtil.checkUpdate(baseRoleMapper.updateByPrimaryKeySelective(param));
            log.info("update role:{} finished. operator is:{}", roleCode, operator);
        }
    }

    @Override
    public void enable(String roleCode, String operator) throws RoleNotExistsException {
        BaseRole baseRole = baseRoleMapper.selectRoleByCode(roleCode);
        if (baseRole == null) {
            throw new RoleNotExistsException();
        }
        BaseRole param = new BaseRole();
        param.setId(baseRole.getId());
        param.setStatus(BizConstants.STATUS.ENABLE);
        param.setUpdateTime(new Date());
        param.setUpdatePerson(operator);
        BizUtil.checkUpdate(baseRoleMapper.updateByPrimaryKeySelective(param));
        log.info("enable role:{} finished. operator is:{}", roleCode, operator);
    }

    @Override
    public void disable(String roleCode, String operator) throws RoleNotExistsException {
        BaseRole baseRole = baseRoleMapper.selectRoleByCode(roleCode);
        if (baseRole == null) {
            throw new RoleNotExistsException();
        }
        BaseRole param = new BaseRole();
        param.setId(baseRole.getId());
        param.setStatus(BizConstants.STATUS.DISABLE);
        param.setUpdateTime(new Date());
        param.setUpdatePerson(operator);
        BizUtil.checkUpdate(baseRoleMapper.updateByPrimaryKeySelective(param));
        log.info("enable role:{} finished. operator is:{}", roleCode, operator);
    }

    private BaseRoleBO toBaseRoleBO(BaseRole baseRole) {
        BaseRoleBO entity = new BaseRoleBO();
        entity.setId(baseRole.getId());
        entity.setRoleCode(baseRole.getRoleCode());
        entity.setRoleName(baseRole.getRoleName());
        entity.setNote(baseRole.getNote());
        entity.setStatus(baseRole.getStatus());
        entity.setCreateTime(baseRole.getCreateTime());
        entity.setCreatePerson(baseRole.getCreatePerson());
        entity.setUpdateTime(baseRole.getUpdateTime());
        entity.setUpdatePerson(baseRole.getUpdatePerson());
        return entity;
    }

}
