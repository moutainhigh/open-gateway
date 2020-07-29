package org.open.gateway.portal.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miko on 2020/7/23.
 *
 * @author MIKO
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("0000", "OK");

    private final String code;
    private final String message;

}
