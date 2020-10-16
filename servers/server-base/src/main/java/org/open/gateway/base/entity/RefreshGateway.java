package org.open.gateway.base.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by miko on 2020/7/16.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class RefreshGateway implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数
     * apiCode/clientId
     */
    private Set<String> args;

    public RefreshGateway() {
    }

    public RefreshGateway(Set<String> args) {
        this.args = args;
    }

    public boolean isRefreshAll() {
        return this.args == null || this.args.isEmpty();
    }

}
