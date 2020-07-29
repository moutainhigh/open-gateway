package org.open.gateway.portal.vo;

import lombok.Data;
import org.open.gateway.portal.constants.ResultCode;

@Data
public class Response {

    private String code;

    private String msg;

    private Object data;

    private PageInfo pageInfo;

    public Response(String code, String msg, Object data, PageInfo pageInfo) {
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

    public static Response ok() {
        return builder().ok();
    }

    public static Response fail(ResultCode resultCode) {
        return builder().fail(resultCode);
    }

    public static class ResponseBuilder {

        private String msg;

        private Object data;

        private PageInfo pageInfo;

        public ResponseBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public ResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseBuilder pageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
            return this;
        }

        private Response code(String resultCode) {
            return new Response(resultCode, msg, data, pageInfo);
        }

        public Response ok() {
            return code(ResultCode.SUCCESS.getCode());
        }

        public Response fail(ResultCode resultCode) {
            return code(resultCode.getCode());
        }

    }

}
