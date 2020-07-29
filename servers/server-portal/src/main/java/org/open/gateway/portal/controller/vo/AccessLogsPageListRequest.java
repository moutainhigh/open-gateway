package org.open.gateway.portal.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.vo.PageRequest;

import java.util.Date;

/**
 * Created by miko on 2020/7/23.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccessLogsPageListRequest extends PageRequest {

    /**
     * 请求ip
     */
    private String ip;

    /**
     * api编码
     */
    private String apiCode;

    /**
     * 路由编码
     */
    private String routeCode;

    /**
     * 请求时间开始
     */
    private Date requestTimeBegin;

    /**
     * 请求时间结束
     */
    private Date requestTimeEnd;

    /**
     * 耗费时间开始
     */
    private Long usedTimeBegin;

    /**
     * 耗费时间结束
     */
    private Long usedTimeEnd;

}
