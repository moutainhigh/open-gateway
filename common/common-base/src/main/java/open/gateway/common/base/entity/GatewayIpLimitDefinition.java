package open.gateway.common.base.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class GatewayIpLimitDefinition {

    /**
     * 黑白名单策略id
     */
    private Integer id;

    /**
     * 黑白名单策略名称
     */
    private String policyName;

    /**
     * 策略类型 black-拒绝/黑名单 white-允许/白名单
     */
    private String policyType;

    /**
     * ip地址 多个用,隔开
     */
    private String ipAddresses;

    /**
     * 状态 0-无效 1-有效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 是否已删除 1已删除，0未删除
     */
    private Byte isDel;

}