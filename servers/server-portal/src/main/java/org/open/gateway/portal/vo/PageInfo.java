package org.open.gateway.portal.vo;

import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 2019/12/13.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class PageInfo {

    /**
     * 页码，从1开始
     */
    @Schema(description = "页码(从1开始)", example = "1")
    private int pageNum;
    /**
     * 页大小
     */
    @Schema(description = "页大小", example = "20")
    private int pageSize;
    /**
     * 总条数
     */
    @Schema(description = "总条数", example = "87")
    private long total;
    /**
     * 总页数
     */
    @Schema(description = "总页数", example = "5")
    private int pages;

    public static PageInfo of(Page<?> page) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(page.getPageNum());
        pageInfo.setPageSize(page.getPageSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        return pageInfo;
    }

}
