package open.gateway.common.utils.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import open.gateway.common.base.entity.token.AccessToken;
import open.gateway.common.utils.IdUtil;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.Map;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
public class JwtEncoder {

    private JWSHeader header;
    private String issuer;
    private RSASSASigner signer;
    private RSAPrivateKey privateKey;

    private AccessToken _generateToken(String subject, long expireTime, Map<String, Object> claims) throws JOSEException {
        // tokenId
        String jwtID = IdUtil.uuid();
        // 1. 建立payload 载体
        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                .issuer(this.issuer)
                .subject(subject)
                .jwtID(jwtID)
                .expirationTime(new Date(System.currentTimeMillis() + expireTime));
        if (claims != null) {
            claims.forEach(claimsSetBuilder::claim);
        }
        JWTClaimsSet claimsSet = claimsSetBuilder.build();
        // 2. 建立签名
        SignedJWT signedJWT = new SignedJWT(this.header, claimsSet);
        signedJWT.sign(this.signer);
        // 3. 生成token
        String tokenValue = signedJWT.serialize();
        AccessToken token = new AccessToken();
        token.setToken(tokenValue);
        token.setExpire_in(expireTime);
        token.setJti(jwtID);
        return token;
    }

    public AccessToken generateToken(String subject, long expireTime, Map<String, Object> claims) {
        try {
            return _generateToken(subject, expireTime, claims);
        } catch (JOSEException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public AccessToken generateToken(String subject, long expireTime) {
        return generateToken(subject, expireTime, null);
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setHeader(JWSHeader header) {
        this.header = header;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setSigner(RSASSASigner signer) {
        this.signer = signer;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        if (privateKey == null) {
            throw new IllegalArgumentException("PrivateKey is null");
        }
        this.privateKey = privateKey;
    }
}
