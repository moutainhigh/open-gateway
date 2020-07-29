package open.gateway.common.base.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class AccessLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求访问的地址
     */
    private String path;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * http响应状态
     */
    private Integer httpStatus;

    /**
     * http请求方法
     */
    private String httpMethod;

    /**
     * 请求头
     */
    private String httpHeaders;

    /**
     * url请求参数
     */
    private String requestQueryString;

    /**
     * 请求body参数
     */
    private String requestBody;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 响应时间
     */
    private Date responseTime;

    /**
     * 请求耗时
     */
    private Integer usedTime;

    /**
     * 用户客户端
     */
    private String userAgent;

    /**
     * 请求者区域
     */
    private String region;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 请求访问的接口编码
     */
    private String apiCode;

    /**
     * 请求访问的路由编码
     */
    private String routeCode;

}