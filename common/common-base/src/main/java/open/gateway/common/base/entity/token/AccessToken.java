package open.gateway.common.base.entity.token;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccessToken {

    private String token;
    private long expire_in;
    private String jti;
}
