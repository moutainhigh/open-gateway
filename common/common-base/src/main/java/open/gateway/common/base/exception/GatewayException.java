package open.gateway.common.base.exception;

/**
 * Created by miko on 2020/7/9.
 * 网关异常
 *
 * @author MIKO
 */
public class GatewayException extends RuntimeException {

    public GatewayException(String msg) {
        super(msg);
    }

    public GatewayException(Exception e) {
        super(e);
    }

    public GatewayException(String msg, Exception e) {
        super(msg, e);
    }
}
