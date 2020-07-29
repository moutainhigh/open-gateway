package open.gateway.common.utils.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

public class JwtDecoder {

    private String issuer;
    private RSASSAVerifier verifier;
    private RSAPublicKey publicKey;

    private JWTClaimsSet _parseToken(String token) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        // 校验是否有效
        if (!jwt.verify(this.verifier)) {
            throw new IllegalArgumentException("Invalid token value:" + token);
        }
        JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
        if (this.issuer != null) {
            if (!this.issuer.equals(jwtClaimsSet.getIssuer())) {
                throw new IllegalArgumentException("Invalid issuer:" + this.issuer);
            }
        }
        Date expirationTime = jwtClaimsSet.getExpirationTime();
        if (expirationTime != null) {
            // 校验超时
            if (new Date().after(expirationTime)) {
                throw new IllegalStateException("Token is expired at " + expirationTime);
            }
        }
        // 获取载体中的数据
        return jwtClaimsSet;
    }

    public JWTClaimsSet parseToken(String token) {
        try {
            return _parseToken(token);
        } catch (ParseException | JOSEException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setVerifier(RSASSAVerifier verifier) {
        this.verifier = verifier;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        if (publicKey == null) {
            throw new IllegalArgumentException("PublicKey is null");
        }
        this.publicKey = publicKey;
    }
}