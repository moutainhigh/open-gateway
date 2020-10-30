package org.open.gateway.portal.modules.monitor.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miko on 10/30/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayAccessLogsQuery {

    private String ip;
    private String apiCode;
    private String routeCode;
    private Date requestTimeBegin;
    private Date requestTimeEnd;
    private Long usedTimeBegin;
    private Long usedTimeEnd;

}
