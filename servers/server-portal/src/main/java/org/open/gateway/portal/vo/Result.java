package org.open.gateway.portal.vo;

import lombok.Getter;
import org.open.gateway.portal.constants.ResultCode;

@Getter
public class Result {

    private final String code;

    private final String msg;

    private final Object data;

    private final PageInfo pageInfo;

    public Result(String code, String msg, Object data, PageInfo pageInfo) {
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

    public static Result ok() {
        return builder().ok();
    }

    public static Result fail(ResultCode resultCode) {
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

        public Result ok() {
            return ok(ResultCode.SUCCESS.getMessage());
        }

        public Result ok(String msg) {
            return build(ResultCode.SUCCESS.getCode(), msg);
        }

        public Result fail(ResultCode resultCode) {
            return build(resultCode.getCode(), resultCode.getMessage());
        }

        private Result build(String resultCode, String msg) {
            return new Result(resultCode, msg, data, pageInfo);
        }

    }

}
