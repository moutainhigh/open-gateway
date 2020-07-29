package open.gateway.common.utils.jwt;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by miko on 2020/7/7.
 * jwt工具类
 *
 * @author MIKO
 */
public class JwtProvider {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
    private String issuer;

    private JwtProvider() {

    }

    public JwtProvider keyPair(KeyPair keyPair) {
        if (keyPair == null) {
            throw new IllegalArgumentException("KeyPair is null");
        }
        return publicKey(keyPair.getPublic())
                .privateKey(keyPair.getPrivate());
    }

    public JwtProvider publicKey(PublicKey publicKey) {
        if (publicKey != null) {
            if (!(publicKey instanceof RSAPublicKey)) {
                throw new IllegalArgumentException("PublicKey must be an RSA ");
            }
            this.publicKey = (RSAPublicKey) publicKey;
        }
        return this;
    }

    public JwtProvider privateKey(PrivateKey privateKey) {
        if (privateKey != null) {
            if (!(privateKey instanceof RSAPrivateKey)) {
                throw new IllegalArgumentException("PrivateKey must be an RSA ");
            }
            this.privateKey = (RSAPrivateKey) privateKey;
        }
        return this;
    }

    public JwtProvider header(JWSHeader header) {
        if (header == null) {
            throw new IllegalArgumentException("Header is null");
        }
        this.header = header;
        return this;
    }

    public JwtProvider issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public JwtDecoder decoder() {
        JwtDecoder decoder = new JwtDecoder();
        decoder.setIssuer(this.issuer);
        decoder.setPublicKey(this.publicKey);
        decoder.setVerifier(new RSASSAVerifier(this.publicKey));
        return decoder;
    }

    public JwtEncoder encoder() {
        JwtEncoder encoder = new JwtEncoder();
        encoder.setHeader(this.header);
        encoder.setIssuer(this.issuer);
        encoder.setPrivateKey(this.privateKey);
        encoder.setSigner(new RSASSASigner(this.privateKey));
        return encoder;
    }

    public static JwtProvider builder() {
        return new JwtProvider();
    }

}
