package org.open.gateway.portal.modules.account.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.ServiceException;
import org.open.gateway.portal.exception.account.AccountAlreadyExistsException;
import org.open.gateway.portal.exception.account.AccountNotAvailableException;
import org.open.gateway.portal.exception.account.AccountNotExistsException;
import org.open.gateway.portal.exception.account.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.service.AccountResourceService;
import org.open.gateway.portal.modules.account.service.AccountService;
import org.open.gateway.portal.modules.account.service.TokenService;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountBO;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountQuery;
import org.open.gateway.portal.persistence.mapper.BaseAccountMapperExt;
import org.open.gateway.portal.persistence.mapper.BaseAccountRoleMapperExt;
import org.open.gateway.portal.persistence.po.BaseAccount;
import org.open.gateway.portal.persistence.po.BaseAccountRole;
import org.open.gateway.portal.security.AccountDetails;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private final AccountResourceService accountResourceService;
    private final BaseAccountMapperExt baseAccountMapper;
    private final BaseAccountRoleMapperExt baseAccountRoleMapper;

    @Override
    public BaseAccountBO queryBaseAccount(String account) {
        log.info("parameter account is:{}", account);
        BaseAccount baseAccount = baseAccountMapper.selectByAccount(account);
        if (baseAccount == null) {
            log.info("account:{} no found.", account);
            return null;
        }
        BaseAccountBO baseAccountBO = toBaseAccountBO(baseAccount);
        log.info("result data is:{}", baseAccountBO);
        return baseAccountBO;
    }

    @Override
    public BaseAccountBO queryExistsBaseAccount(String account) throws AccountNotExistsException {
        BaseAccountBO baseAccount = queryBaseAccount(account);
        if (baseAccount == null) {
            throw new AccountNotExistsException();
        }
        return baseAccount;
    }

    @Override
    public BaseAccountBO queryValidBaseAccount(String account) throws AccountNotExistsException, AccountNotAvailableException {
        BaseAccountBO baseAccount = queryExistsBaseAccount(account);
        if (BizConstants.STATUS.ENABLE != baseAccount.getStatus()) {
            throw new AccountNotAvailableException();
        }
        return baseAccount;
    }

    @Override
    public List<BaseAccountBO> queryBaseAccounts(BaseAccountQuery query) {
        List<BaseAccount> baseAccounts = baseAccountMapper.selectByCondition(query);
        log.info("query base accounts result num:{} param:{}", baseAccounts.size(), query);
        return baseAccounts.stream()
                .map(this::toBaseAccountBO)
                .collect(Collectors.toList());
    }

    @Override
    public BaseAccountBO register(String account, String plainPassword, String phone, String email, String note, String registerIp, String operator) throws AccountAlreadyExistsException {
        BaseAccountBO accountBO = queryBaseAccount(account);
        if (accountBO != null) {
            throw new AccountAlreadyExistsException();
        }
        String salt = StringUtil.randomLetter(16); // 生成摘要加密盐
        String digestPassword = BizUtil.generateDigestPassword(plainPassword, salt);
        Date now = new Date();
        BaseAccount param = new BaseAccount();
        param.setAccount(account);
        param.setPassword(digestPassword);
        param.setSalt(salt);
        param.setRegisterIp(registerIp);
        param.setStatus(BizConstants.STATUS.ENABLE);
        param.setPhone(phone);
        param.setEmail(email);
        param.setNote(note);
        param.setCreateTime(now);
        param.setCreatePerson(operator);
        param.setIsDel(BizConstants.DEL_FLAG.NO);
        BizUtil.checkUpdate(baseAccountMapper.insertSelective(param));
        log.info("register finished. account:{} operator:{}", account, operator);
        return toBaseAccountBO(param);
    }

    @Transactional(rollbackFor = {ServiceException.class, RuntimeException.class})
    @Override
    public void update(String account, String plainPassword, String phone, String email, String note, String operator, List<Integer> roleIds) throws AccountNotExistsException, AccountNotAvailableException {
        BaseAccountBO accountBO = queryValidBaseAccount(account);
        Date now = new Date();
        BaseAccount param = new BaseAccount();
        param.setId(accountBO.getId());
        // 传了密码就更新密码
        if (StringUtil.isNotBlank(plainPassword)) {
            String salt = StringUtil.randomLetter(16); // 生成摘要加密盐
            String digestPassword = BizUtil.generateDigestPassword(plainPassword, salt);
            param.setPassword(digestPassword);
            param.setSalt(salt);
        }
        param.setPhone(phone);
        param.setEmail(email);
        param.setNote(note);
        param.setUpdateTime(now);
        param.setUpdatePerson(operator);
        BizUtil.checkUpdate(baseAccountMapper.updateByPrimaryKeySelective(param));
        log.info("update account:{} finished", account);
        if (roleIds != null) {
            BizUtil.checkUpdate(baseAccountRoleMapper.deleteByAccountId(accountBO.getId()));
            log.info("delete exists role by account id:{} account:{}", accountBO.getId(), accountBO.getAccount());
            if (!roleIds.isEmpty()) {
                BizUtil.checkUpdate(baseAccountRoleMapper.insertBatch(roleIds.stream()
                        .map(roleId -> toBaseAccountRole(operator, accountBO, roleId))
                        .collect(Collectors.toList())), roleIds.size());
                log.info("insert roles finished. num:{} account:{}", roleIds.size(), accountBO.getAccount());
            }
            tokenService.updateToken(toAccountDetails(accountBO));
        }
        log.info("update transactional finished. account:{} operator:{}", account, operator);
    }

    @Override
    public void enable(String account, String operator) throws AccountNotExistsException {
        // 查询存在的账户
        BaseAccountBO accountBO = queryExistsBaseAccount(account);
        BaseAccount param = new BaseAccount();
        param.setId(accountBO.getId());
        param.setStatus(BizConstants.STATUS.ENABLE);
        param.setUpdateTime(new Date());
        param.setUpdatePerson(operator);
        BizUtil.checkUpdate(baseAccountMapper.updateByPrimaryKeySelective(param));
        log.info("enable account:{} finished. operator is:{}", accountBO.getAccount(), operator);
    }

    @Override
    public void disable(String account, String operator) throws AccountNotExistsException {
        // 查询存在的账户
        BaseAccountBO accountBO = queryExistsBaseAccount(account);
        BaseAccount param = new BaseAccount();
        param.setId(accountBO.getId());
        param.setStatus(BizConstants.STATUS.DISABLE);
        param.setUpdateTime(new Date());
        param.setUpdatePerson(operator);
        BizUtil.checkUpdate(baseAccountMapper.updateByPrimaryKeySelective(param));
        log.info("updated account:{} status to disable status:{}", account, param.getStatus());
        tokenService.deleteToken(account);
        log.info("disable account:{} finished. operator is:{}", accountBO.getAccount(), operator);
    }

    @Override
    public String login(String account, String plainPassword) throws AccountPasswordInvalidException, AccountNotExistsException, AccountNotAvailableException {
        // 查询账号
        BaseAccountBO accountBO = queryValidBaseAccount(account);
        // 校验密码
        checkAccountPassword(plainPassword, accountBO.getSalt(), accountBO.getPassword());
        // 获取账户权限信息
        AccountDetails accountDetails = toAccountDetails(accountBO);
        // 生成token
        return tokenService.generateToken(accountDetails);
    }

    @Override
    public void logout(String account) {
        Boolean result = tokenService.deleteToken(account);
        log.info("logout finished by account:{} result:{}", account, result);
    }

    @Override
    public void delete(String account, String operator) throws AccountNotExistsException {
        BaseAccountBO accountBO = queryExistsBaseAccount(account);
        BaseAccount param = new BaseAccount();
        param.setId(accountBO.getId());
        param.setUpdatePerson(operator);
        param.setUpdateTime(new Date());
        param.setIsDel(BizConstants.DEL_FLAG.YES);
        BizUtil.checkUpdate(baseAccountMapper.updateByPrimaryKeySelective(param), 1);
        log.info("logic delete account:{} finished. operator is:{}", account, operator);
    }

    /**
     * 校验账户秘密
     *
     * @param plainPassword  密码明文
     * @param salt           摘要加密盐
     * @param digestPassword 密码密文
     * @throws AccountPasswordInvalidException 无效的账户密码
     */
    private void checkAccountPassword(String plainPassword, String salt, String digestPassword) throws AccountPasswordInvalidException {
        String newDigestPassword = BizUtil.generateDigestPassword(plainPassword, salt);
        if (!newDigestPassword.equals(digestPassword)) {
            throw new AccountPasswordInvalidException();
        }
        log.info("check account password finished");
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

    private AccountDetails toAccountDetails(BaseAccountBO baseAccountBO) {
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setId(baseAccountBO.getId());
        accountDetails.setAccount(baseAccountBO.getAccount());
        accountDetails.setPassword(baseAccountBO.getPassword());
        // 获取资源
        Set<String> perms = accountResourceService.queryPermsByAccount(baseAccountBO.getAccount());
        accountDetails.setAuthorities(perms);
        return accountDetails;
    }

    private BaseAccountRole toBaseAccountRole(String operator, BaseAccountBO accountBO, Integer roleId) {
        BaseAccountRole baseAccountRole = new BaseAccountRole();
        baseAccountRole.setCreateTime(new Date());
        baseAccountRole.setCreatePerson(operator);
        baseAccountRole.setAccountId(accountBO.getId());
        baseAccountRole.setRoleId(roleId);
        return baseAccountRole;
    }

}
