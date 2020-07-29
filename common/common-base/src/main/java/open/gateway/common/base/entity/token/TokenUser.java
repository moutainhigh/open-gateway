package open.gateway.common.base.entity.token;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

/**
 * Created by miko on 2020/7/9.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class TokenUser {

    private String clientId;
    private Collection<String> authorities;
    private Collection<String> scopes;

}
