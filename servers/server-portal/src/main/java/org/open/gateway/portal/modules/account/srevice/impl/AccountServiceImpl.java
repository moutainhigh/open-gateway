package org.open.gateway.portal.modules.account.srevice.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.exception.AccountExistsException;
import org.open.gateway.portal.exception.AccountNotAvailableException;
import org.open.gateway.portal.exception.AccountNotExistsException;
import org.open.gateway.portal.exception.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.srevice.AccountService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.persistence.mapper.BaseAccountMapperExt;
import org.open.gateway.portal.persistence.po.BaseAccount;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private final StringRedisTemplate redisTemplate;
    private final BaseAccountMapperExt baseAccountMapper;

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
        return storeToken(accountBO);
    }

    @Override
    public void logout(String token) {
        String tokenKey = getRedisKey(token);
        Boolean result = redisTemplate.delete(tokenKey);
        log.info("logout finished delete tokenKey:{} result:{}", tokenKey, result);
    }


    private void checkAccountPassword(String plainPassword, String salt, String secretPassword) throws AccountPasswordInvalidException {
        String tempSecretPassword = BizUtil.getSecretPassword(plainPassword, salt);
        if (!tempSecretPassword.equals(secretPassword)) {
            throw new AccountPasswordInvalidException();
        }
        log.info("check account password finished");
    }

    private String storeToken(BaseAccountBO account) {
        String token = BizUtil.generateToken(account.getAccount(), account.getPassword());
        log.info("generated token is:{}", token);
        Boolean result = redisTemplate.opsForValue().setIfAbsent(getRedisKey(token), JSON.toJSONString(account), Duration.ofMinutes(120));
        log.info("store token into redis finished. result:{}", result);
        return token;
    }

    private String getRedisKey(String token) {
        return BizConstants.TOKEN_PREFIX + token;
    }

}
