package org.open.gateway.portal.modules.account.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.account.RoleNotExistsException;
import org.open.gateway.portal.modules.account.service.AccountRoleService;
import org.open.gateway.portal.modules.account.service.bo.BaseRoleBO;
import org.open.gateway.portal.persistence.mapper.BaseRoleMapperExt;
import org.open.gateway.portal.persistence.mapper.BaseRoleResourceMapperExt;
import org.open.gateway.portal.persistence.po.BaseRole;
import org.open.gateway.portal.persistence.po.BaseRoleResource;
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
    private final BaseRoleResourceMapperExt baseRoleResourceMapper;

    @Override
    public List<BaseRoleBO> queryRolesByAccount(String account) {
        List<BaseRole> baseRoles = baseRoleMapper.selectByAccount(account);
        log.info("query roles num:{} by account:{}", baseRoles.size(), account);
        return baseRoles.stream()
                .map(this::toBaseRoleBO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void save(String roleCode, String roleName, String note, String operator, List<Integer> resourceIds) {
        BaseRole role = baseRoleMapper.selectByCode(roleCode);
        if (role == null) {
            log.info("role code:{} not exists. starting insert.", roleCode);
            role = new BaseRole();
            role.setRoleCode(roleCode);
            role.setRoleName(roleName);
            role.setNote(note);
            role.setCreateTime(new Date());
            role.setCreatePerson(operator);
            BizUtil.checkUpdate(baseRoleMapper.insertSelective(role));
            log.info("insert role:{} finished. operator is:{} new id is:{}", roleCode, operator, role.getId());
        } else {
            log.info("role code:{} exists. starting update id:{}.", roleCode, role.getId());
            role.setRoleName(roleName);
            role.setNote(note);
            role.setUpdateTime(new Date());
            role.setUpdatePerson(operator);
            BizUtil.checkUpdate(baseRoleMapper.updateByPrimaryKeySelective(role));
            log.info("update role:{} finished. operator is:{}", roleCode, operator);
        }
        if (resourceIds != null) {
            Integer roleId = role.getId();
            int count = baseRoleResourceMapper.deleteByRoleId(roleId);
            log.info("delete exists resource by role id:{} delete count:{} operator:{}", roleId, count, operator);
            if (!resourceIds.isEmpty()) {
                BizUtil.checkUpdate(baseRoleResourceMapper.insertBatch(
                        resourceIds.stream()
                                .map(rid -> this.toBaseRoleResource(roleId, rid, operator))
                                .collect(Collectors.toList())
                ), resourceIds.size());
                log.info("insert batch resources finished. operator is:{}", operator);
            }
        }
    }

    @Override
    public void enable(String roleCode, String operator) throws RoleNotExistsException {
        BaseRole baseRole = baseRoleMapper.selectByCode(roleCode);
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
        BaseRole baseRole = baseRoleMapper.selectByCode(roleCode);
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

    @Override
    public void delete(String roleCode, String operator) throws RoleNotExistsException {
        BaseRole baseRole = baseRoleMapper.selectByCode(roleCode);
        if (baseRole == null) {
            throw new RoleNotExistsException();
        }
        BizUtil.checkUpdate(baseRoleMapper.deleteByPrimaryKey(baseRole.getId()));
        log.info("delete role:{} finished. id is:{} operator is:{}", roleCode, baseRole.getId(), operator);
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

    private BaseRoleResource toBaseRoleResource(Integer roleId, Integer resourceId, String operator) {
        BaseRoleResource response = new BaseRoleResource();
        response.setCreateTime(new Date());
        response.setCreatePerson(operator);
        response.setRoleId(roleId);
        response.setResourceId(resourceId);
        return response;
    }

}
