package org.open.gateway.route.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class OauthClientToken {

    private Integer id;

    private String clientId;

    private String token;

    private LocalDateTime expireTime;

    public boolean isExpired() {
        return this.expireTime.isBefore(LocalDateTime.now());
    }

}