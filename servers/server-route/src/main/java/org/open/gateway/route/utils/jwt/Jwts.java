package org.open.gateway.route.utils.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import open.gateway.common.base.constants.OAuth2Constants;
import open.gateway.common.utils.IdUtil;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.exception.InvalidTokenException;
import org.open.gateway.route.exception.TokenExpiredException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
public class Jwts {

    private String issuer;
    private RSASSAVerifier verifier;
    private RSAPublicKey publicKey;

    private JWSHeader header;
    private RSASSASigner signer;
    private RSAPrivateKey privateKey;

    /**
     * 解析token
     *
     * @param token token字符串
     * @return token对象
     * @throws ParseException 解析异常
     * @throws JOSEException  签名异常
     */
    private JWTClaimsSet doParseToken(String token) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        // 校验是否有效
        if (!jwt.verify(this.verifier)) {
            throw new InvalidTokenException("verify failed" + token);
        }
        JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
        if (this.issuer != null) {
            if (!this.issuer.equals(jwtClaimsSet.getIssuer())) {
                throw new InvalidTokenException("invalid issuer:" + this.issuer);
            }
        }
        Date expirationTime = jwtClaimsSet.getExpirationTime();
        if (expirationTime != null) {
            // 校验超时
            if (new Date().after(expirationTime)) {
                throw new TokenExpiredException(expirationTime);
            }
        }
        // 获取载体中的数据
        return jwtClaimsSet;
    }

    /**
     * 解析token
     *
     * @param token token字符串
     * @return token对象
     */
    public JWTClaimsSet parseToken(String token) {
        try {
            return doParseToken(token);
        } catch (ParseException | JOSEException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 生成token
     *
     * @param subject  主体内容
     * @param expireAt 过期时间点
     * @param claims   其他信息
     * @return token对象
     * @throws JOSEException 签名异常
     */
    private String doGenerateToken(String subject, long expireAt, Map<String, Object> claims) throws JOSEException {
        // tokenId
        String jwtID = IdUtil.uuid();
        // 1. 建立payload 载体
        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                .issuer(this.issuer)
                .subject(subject)
                .jwtID(jwtID)
                .expirationTime(new Date(expireAt));
        if (claims != null) {
            claims.forEach(claimsSetBuilder::claim);
        }
        JWTClaimsSet claimsSet = claimsSetBuilder.build();
        // 2. 建立签名
        SignedJWT signedJWT = new SignedJWT(this.header, claimsSet);
        signedJWT.sign(this.signer);
        // 3. 生成token
        return signedJWT.serialize();
    }

    /**
     * 生成token
     *
     * @param subject   主体内容
     * @param expireAt  过期时间点
     * @param tokenUser 用户信息
     * @return token对象
     */
    public String generateToken(String subject, long expireAt, TokenUser tokenUser) {
        HashMap<String, Object> claims = new HashMap<>(3);
        claims.put(OAuth2Constants.TokenPayloadKey.SCOPE, tokenUser.getScopes());
        claims.put(OAuth2Constants.TokenPayloadKey.AUTHORITIES, tokenUser.getAuthorities());
        return generateToken(subject, expireAt, claims);
    }

    /**
     * 生成token
     *
     * @param subject  主体内容
     * @param expireAt 过期时间点
     * @param claims   其他信息
     * @return token对象
     */
    public String generateToken(String subject, long expireAt, Map<String, Object> claims) {
        try {
            return doGenerateToken(subject, expireAt, claims);
        } catch (JOSEException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        if (publicKey == null) {
            throw new IllegalArgumentException("PublicKey is null");
        }
        this.publicKey = publicKey;
        this.verifier = new RSASSAVerifier(this.publicKey);
    }

    public void setHeader(JWSHeader header) {
        this.header = header;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        if (privateKey == null) {
            throw new IllegalArgumentException("PrivateKey is null");
        }
        this.privateKey = privateKey;
        this.signer = new RSASSASigner(privateKey);
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

}