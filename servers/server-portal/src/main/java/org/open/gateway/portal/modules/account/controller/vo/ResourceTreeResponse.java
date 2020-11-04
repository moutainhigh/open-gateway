package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by miko on 9/27/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class ResourceTreeResponse implements Comparable<ResourceTreeResponse> {

    @ApiModelProperty(notes = "id", example = "1")
    private Integer id;

    @ApiModelProperty(notes = "资源代码", example = "10")
    private String resourceCode;

    @ApiModelProperty(notes = "资源名称", example = "10")
    private String resourceName;

    @ApiModelProperty(notes = "资源类型", example = "D", allowableValues = "D, M, B")
    private String resourceType;

    @ApiModelProperty(notes = "父资源代码", example = "1")
    private String parentCode;

    @ApiModelProperty(notes = "权限", example = "account:update:post")
    private String perms;

    @ApiModelProperty(notes = "地址", example = "/aaa/bbb/xxx.html")
    private String url;

    @ApiModelProperty(notes = "排序", example = "1")
    private Integer sort;

    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

    @ApiModelProperty(notes = "子节点集合")
    private List<ResourceTreeResponse> children;

    @Override
    public int compareTo(ResourceTreeResponse o) {
        if (o == null || o.getSort() == null) {
            throw new NullPointerException("compare object or sort is null");
        }
        if (this.getSort().equals(o.getSort())) {
            if (this.getParentCode() == null || o.getResourceCode() == null) {
                return 0;
            }
            return this.getParentCode().compareTo(o.getResourceCode());
        }
        return this.getSort() - o.getSort();
    }

}
