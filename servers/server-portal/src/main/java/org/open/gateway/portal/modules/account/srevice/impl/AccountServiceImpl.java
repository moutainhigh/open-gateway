package org.open.gateway.portal.modules.account.srevice.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.AccountAlreadyExistsException;
import org.open.gateway.portal.exception.AccountNotAvailableException;
import org.open.gateway.portal.exception.AccountNotExistsException;
import org.open.gateway.portal.exception.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.TokenService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.persistence.mapper.BaseAccountMapperExt;
import org.open.gateway.portal.persistence.po.BaseAccount;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public BaseAccountBO queryValidBaseAccountByCode(String account) throws AccountNotExistsException, AccountNotAvailableException {
        BaseAccountBO baseAccount = queryBaseAccount(account);
        if (baseAccount == null) {
            throw new AccountNotExistsException();
        }
        if (BizConstants.STATUS.NORMAL != baseAccount.getStatus()) {
            throw new AccountNotAvailableException();
        }
        return baseAccount;
    }

    @Override
    public BaseAccountBO register(String account, String plainPassword, String phone, String email, String note, String registerIp, String operator) throws AccountNotAvailableException, AccountAlreadyExistsException {
        BaseAccountBO accountBO = queryBaseAccount(account);
        if (accountBO != null) {
            throw new AccountAlreadyExistsException();
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
        baseAccount.setCreatePerson(operator);
        baseAccount.setIsDel(BizConstants.DEL_FLAG.NO);
        BizUtil.checkUpdate(baseAccountMapper.insertSelective(baseAccount));
        log.info("register finished. account:{} operator:{}", account, operator);
        return toBaseAccountBO(baseAccount);
    }

    @Override
    public void update(String account, String plainPassword, String phone, String email, String note, Byte status, String operator) throws AccountNotExistsException, AccountNotAvailableException {
        BaseAccountBO accountBO = queryValidBaseAccountByCode(account);
        Date now = new Date();
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setId(accountBO.getId());
        // 传了密码就更新密码
        if (StringUtil.isNotBlank(plainPassword)) {
            String salt = StringUtil.randomLetter(16); // 生成摘要加密盐
            String secretPassword = BizUtil.getSecretPassword(plainPassword, salt);
            baseAccount.setPassword(secretPassword);
            baseAccount.setSalt(salt);
        }
        baseAccount.setStatus(status);
        baseAccount.setPhone(phone);
        baseAccount.setEmail(email);
        baseAccount.setNote(note);
        baseAccount.setUpdateTime(now);
        baseAccount.setUpdatePerson(operator);
        BizUtil.checkUpdate(baseAccountMapper.updateByPrimaryKeySelective(baseAccount));
        log.info("update finished. account:{} operator:{}", account, operator);
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
        BaseAccountBO accountBO = queryValidBaseAccountByCode(account);
        // 校验密码
        checkAccountPassword(plainPassword, accountBO.getSalt(), accountBO.getPassword());
        // 生成token
        return tokenService.generateToken(accountBO);
    }

    @Override
    public void logout(String account) {
        Boolean result = tokenService.deleteToken(account);
        log.info("logout finished by account:{} result:{}", account, result);
    }

    @Override
    public void delete(String account, String operator) throws AccountNotExistsException {
        BaseAccountBO accountBO = queryBaseAccount(account);
        if (accountBO == null) {
            throw new AccountNotExistsException();
        }
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setId(accountBO.getId());
        baseAccount.setUpdatePerson(operator);
        baseAccount.setUpdateTime(new Date());
        baseAccount.setIsDel(BizConstants.DEL_FLAG.YES);
        BizUtil.checkUpdate(baseAccountMapper.updateByPrimaryKeySelective(baseAccount), 1);
        log.info("logic delete account:{} finished. operator is:{}", account, operator);
    }

    /**
     * 校验账户秘密
     *
     * @param plainPassword  密码明文
     * @param salt           摘要加密盐
     * @param secretPassword 密码密文
     * @throws AccountPasswordInvalidException 无效的账户密码
     */
    private void checkAccountPassword(String plainPassword, String salt, String secretPassword) throws AccountPasswordInvalidException {
        String tempSecretPassword = BizUtil.getSecretPassword(plainPassword, salt);
        if (!tempSecretPassword.equals(secretPassword)) {
            throw new AccountPasswordInvalidException();
        }
        log.info("check account password finished");
    }

}
