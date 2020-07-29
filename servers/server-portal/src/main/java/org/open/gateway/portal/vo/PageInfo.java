package org.open.gateway.portal.vo;

import com.github.pagehelper.Page;
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
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
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
