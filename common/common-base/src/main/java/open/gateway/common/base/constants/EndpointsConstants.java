package open.gateway.common.base.constants;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
public interface EndpointsConstants {

    // 获取jwt签名公钥
    String JWKS = "/.well-known/jwks.json";
    // 获取token的地址
    String TOKEN = "/oauth/token";
    // 认证地址
    String AUTHORIZE = "/oauth/authorize";

}
