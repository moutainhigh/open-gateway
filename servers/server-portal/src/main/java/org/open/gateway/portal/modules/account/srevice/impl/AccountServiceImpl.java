package org.open.gateway.portal.modules.account.srevice.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.AccountExistsException;
import org.open.gateway.portal.exception.AccountNotAvailableException;
import org.open.gateway.portal.exception.AccountNotExistsException;
import org.open.gateway.portal.exception.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.TokenService;
import org.open.gateway.portal.modules.account.srevice.bo.AccountResourceBO;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.persistence.mapper.BaseAccountMapperExt;
import org.open.gateway.portal.persistence.mapper.BaseResourceMapperExt;
import org.open.gateway.portal.persistence.po.BaseAccount;
import org.open.gateway.portal.persistence.po.BaseResource;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final TokenService tokenService;
    private final BaseAccountMapperExt baseAccountMapper;
    private final BaseResourceMapperExt baseResourceMapper;

    @Override
    public BaseAccountBO queryBaseAccountByCode(String account) throws AccountNotAvailableException {
        log.info("parameter account is:{}", account);
        BaseAccount baseAccount = baseAccountMapper.selectByAccount(account);
        if (baseAccount == null) {
            log.info("account no found. param account is:{}", account);
            return null;
        }
        if (BizConstants.STATUS.NORMAL != baseAccount.getStatus()) {
            throw new AccountNotAvailableException();
        }
        BaseAccountBO baseAccountBO = toBaseAccountBO(baseAccount);
        log.info("result data is:{}", baseAccountBO);
        return baseAccountBO;
    }

    @Override
    public BaseAccountBO register(String account, String plainPassword, String phone, String email, String note, String registerIp) throws AccountNotAvailableException, AccountExistsException {
        BaseAccountBO accountBO = queryBaseAccountByCode(account);
        if (accountBO != null) {
            throw new AccountExistsException();
        }
        String salt = StringUtil.randomLetter(16); // 生成摘要加密盐
        String secretPassword = BizUtil.getSecretPassword(plainPassword, salt);
        Date now = new Date();
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setAccount(account);
        baseAccount.setPassword(secretPassword);
        baseAccount.setSalt(salt);
        baseAccount.setRegisterIp(registerIp);
        baseAccount.setStatus(BizConstants.STATUS.NORMAL);
        baseAccount.setPhone(phone);
        baseAccount.setEmail(email);
        baseAccount.setNote(note);
        baseAccount.setCreateTime(now);
        baseAccount.setCreatePerson(BizConstants.DEFAULT_OPERATE_PERSON);
        baseAccount.setUpdateTime(now);
        baseAccount.setUpdatePerson(BizConstants.DEFAULT_OPERATE_PERSON);
        baseAccount.setIsDel(BizConstants.DEL_FLAG.NO);
        BizUtil.checkUpdate(baseAccountMapper.insertSelective(baseAccount));
        return toBaseAccountBO(baseAccount);
    }

    private BaseAccountBO toBaseAccountBO(BaseAccount baseAccount) {
        BaseAccountBO baseAccountBO = new BaseAccountBO();
        baseAccountBO.setId(baseAccount.getId());
        baseAccountBO.setAccount(baseAccount.getAccount());
        baseAccountBO.setPassword(baseAccount.getPassword());
        baseAccountBO.setSalt(baseAccount.getSalt());
        baseAccountBO.setRegisterIp(baseAccount.getRegisterIp());
        baseAccountBO.setStatus(baseAccount.getStatus());
        baseAccountBO.setPhone(baseAccount.getPhone());
        baseAccountBO.setEmail(baseAccount.getEmail());
        baseAccountBO.setNote(baseAccount.getNote());
        baseAccountBO.setCreateTime(baseAccount.getCreateTime());
        baseAccountBO.setCreatePerson(baseAccount.getCreatePerson());
        baseAccountBO.setUpdateTime(baseAccount.getUpdateTime());
        baseAccountBO.setUpdatePerson(baseAccount.getUpdatePerson());
        baseAccountBO.setIsDel(baseAccount.getIsDel());
        return baseAccountBO;
    }

    @Override
    public String login(String account, String plainPassword) throws AccountPasswordInvalidException, AccountNotExistsException, AccountNotAvailableException {
        // 查询账号
        BaseAccountBO accountBO = queryBaseAccountByCode(account);
        if (accountBO == null) {
            throw new AccountNotExistsException();
        }
        // 校验密码
        checkAccountPassword(plainPassword, accountBO.getSalt(), accountBO.getPassword());
        // 生成token
        return tokenService.storeToken(accountBO);
    }

    @Override
    public void logout(String account, String token) {
        Boolean result = tokenService.deleteToken(account, token);
        log.info("logout finished by token:{} result:{}", token, result);
    }

    @Override
    public List<AccountResourceBO> queryResourcesByAccount(String account) {
        // 查询所有资源
        List<BaseResource> resources = baseResourceMapper.selectResourcesByAccount(account);
        log.info("account:{} resources num:{}", account, resources.size());
        // 按照父代码分组
        Map<String, List<AccountResourceBO>> resourcesGroup = resources.stream()
                .map(this::toAccountResourceBO)
                .collect(Collectors.groupingBy(AccountResourceBO::getParentCode));
        // 根节点
        List<AccountResourceBO> rootResources = toResourceTree(BizConstants.ROOT_CODE, resourcesGroup);
        log.info("root resources num:{}", rootResources.size());
        return rootResources;
    }

    private AccountResourceBO toAccountResourceBO(BaseResource br) {
        AccountResourceBO ar = new AccountResourceBO();
        ar.setResourceCode(br.getResourceCode());
        ar.setResourceName(br.getResourceName());
        ar.setParentCode(br.getParentCode());
        ar.setUrl(br.getUrl());
        ar.setSort(br.getSort());
        ar.setNote(br.getNote());
        return ar;
    }

    private List<AccountResourceBO> toResourceTree(String parentCode, Map<String, List<AccountResourceBO>> resourcesGroup) {
        List<AccountResourceBO> resources = resourcesGroup.getOrDefault(parentCode, new ArrayList<>());
        if (resources != null) {
            for (AccountResourceBO r : resources) {
                r.setChildren(toResourceTree(r.getResourceCode(), resourcesGroup));
            }
        }
        return resources;
    }

    private void checkAccountPassword(String plainPassword, String salt, String secretPassword) throws AccountPasswordInvalidException {
        String tempSecretPassword = BizUtil.getSecretPassword(plainPassword, salt);
        if (!tempSecretPassword.equals(secretPassword)) {
            throw new AccountPasswordInvalidException();
        }
        log.info("check account password finished");
    }

}
