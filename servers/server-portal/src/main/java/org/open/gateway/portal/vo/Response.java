package org.open.gateway.portal.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.open.gateway.portal.constants.ResultCode;

@ApiModel(description = "基础返回对象")
@Getter
public class Response<T> {

    @ApiModelProperty(notes = "代码", example = "0000")
    private final String code;

    @ApiModelProperty(notes = "消息", example = "成功")
    private final String msg;

    @ApiModelProperty(notes = "业务数据")
    private final T data;

    public Response(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Response.ResponseBuilder builder() {
        return new Response.ResponseBuilder();
    }

    public static Response.ResponseBuilder data(Object data) {
        return builder().data(data);
    }

    public static Response<Void> ok() {
        return builder().ok();
    }

    public static Response<Void> fail(ResultCode resultCode) {
        return builder().fail(resultCode);
    }

    public static class ResponseBuilder {

        private Object data;

        public Response.ResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public <T> Response<T> ok() {
            return ok(ResultCode.SUCCESS.getMessage());
        }

        public <T> Response<T> ok(String msg) {
            return build(ResultCode.SUCCESS.getCode(), msg);
        }

        public <T> Response<T> fail(ResultCode resultCode) {
            return build(resultCode.getCode(), resultCode.getMessage());
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private <T> Response<T> build(String resultCode, String msg) {
            return new Response(resultCode, msg, data);
        }

    }

}
