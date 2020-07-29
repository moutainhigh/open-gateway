package org.open.gateway.portal.vo;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by miko on 2019/10/29.
 *
 * @author MIKO
 */
@Getter
@Setter
public class PageRequest {

    /**
     * 页码 从1开始
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum;
    /**
     * 页大小
     */
    @NotNull(message = "页大小不能为空")
    @Min(value = 1, message = "页大小不能小于1")
    private Integer pageSize;
    /**
     * 排序依据
     */
    private String sortBy;
    /**
     * 排序方式 asc/desc
     */
    private String order;

    /**
     * 是否进行count查询
     */
    private boolean count = false;

    /**
     * 移除本地变量
     */
    public void clearPage() {
        PageHelper.clearPage();
    }

    /**
     * 获取任意查询方法的count总数
     *
     * @param select 查询代码
     * @return 总数
     */
    public static long count(ISelect select) {
        return PageHelper.count(select);
    }

    /**
     * 开始分页
     */
    public <E> Page<E> startPage() {
        if (!StringUtils.isBlank(sortBy)) {
            // 校验支持的类型
            Set<SortBy> supportedSortBys = supportedSortBys();
            if (supportedSortBys != null && !supportedSortBys.isEmpty()) {
                SortBy sort = supportedSortBys.stream()
                        .filter(sb -> sb.getKey().equals(sortBy))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No supported sort by key:" + sortBy + " in " + supportedSortBys + ""));
                String newSortBy = sort.getSortBy();
                // 设置排序
                if (!StringUtils.isBlank(order)) {
                    PageHelper.orderBy(newSortBy + " " + SortBy.SortType.from(order).name());
                } else {
                    PageHelper.orderBy(newSortBy);
                }
            }
        }
        return PageHelper.startPage(pageNum, pageSize, count);
    }

    public <E> Page<E> doSelectPage(ISelect select) {
        return startPage().doSelectPage(select);
    }

    /**
     * 支持的排序字段，为null或者空不使用排序
     *
     * @return 支持的排序字段
     */
    protected Set<SortBy> supportedSortBys() {
        return null;
    }

}
