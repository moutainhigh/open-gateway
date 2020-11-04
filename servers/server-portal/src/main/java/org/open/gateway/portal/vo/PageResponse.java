package org.open.gateway.portal.vo;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.open.gateway.portal.constants.ResultCode;

@ApiModel(description = "分页信息基础返回对象")
@Getter
public class PageResponse<T> {

    @ApiModelProperty(notes = "代码", example = "0000")
    private final String code;

    @ApiModelProperty(notes = "消息", example = "成功")
    private final String msg;

    @ApiModelProperty(notes = "业务数据")
    private final T data;

    @ApiModelProperty(notes = "分页数据")
    private final PageInfo pageInfo;

    public PageResponse(String code, String msg, T data, PageInfo pageInfo) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.pageInfo = pageInfo;
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static ResponseBuilder data(Object data) {
        return builder().data(data);
    }

    public static ResponseBuilder pageInfo(PageInfo pageInfo) {
        return builder().pageInfo(pageInfo);
    }

    public static PageResponse<Void> ok() {
        return builder().ok();
    }

    public static PageResponse<Void> fail(ResultCode resultCode) {
        return builder().fail(resultCode);
    }

    public static class ResponseBuilder {

        private Object data;

        private PageInfo pageInfo;

        public ResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseBuilder pageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
            return this;
        }

        public ResponseBuilder pageInfo(Page<?> page) {
            return pageInfo(PageInfo.of(page));
        }

        public <T> PageResponse<T> ok() {
            return ok(ResultCode.SUCCESS.getMessage());
        }

        public <T> PageResponse<T> ok(String msg) {
            return build(ResultCode.SUCCESS.getCode(), msg);
        }

        public <T> PageResponse<T> fail(ResultCode resultCode) {
            return build(resultCode.getCode(), resultCode.getMessage());
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private <T> PageResponse<T> build(String resultCode, String msg) {
            return new PageResponse(resultCode, msg, data, pageInfo);
        }

    }

}
