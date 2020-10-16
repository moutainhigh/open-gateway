package com.baosight.portal.util;

import com.baosight.portal.BaseTest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by miko on 9/28/20.
 *
 * @author MIKO
 */
@Slf4j
public class JsonTest extends BaseTest {

    @Test
    public void testLocalDate() {
        LocalDateTime requestTime = LocalDateTime.parse("2020-09-29 10:39:10", DateTimeFormatters.yyyy_MM_dd_HH_mm_ss);
        DateBean request = new DateBean();
        request.setLocalDateTime(requestTime);
        String requestBody = JSON.toJSONString(request);
        log.info("serialize json str is:{}", requestBody);
        DateBean newRequest = JSON.parse(requestBody, DateBean.class);
        log.info("deserialize obj is:{}", newRequest);
        String newRequestBody = JSON.toJSONString(request);
        log.info("serialize json str is:{}", newRequestBody);
        Assert.isTrue(objectEq(requestBody, newRequestBody), "json serialize error");
    }

    @Test
    public void testUtilDate() {
        DateBean request = new DateBean();
        request.setUtilDate(new Date());
        String requestBody = JSON.toJSONString(request);
        log.info("serialize json str is:{}", requestBody);
        DateBean newRequest = JSON.parse(requestBody, DateBean.class);
        log.info("deserialize obj is:{}", newRequest);
        String newRequestBody = JSON.toJSONString(request);
        log.info("serialize json str is:{}", newRequestBody);
        Assert.isTrue(objectEq(requestBody, newRequestBody), "json serialize error");
    }

    @Data
    static class DateBean {

        private String id;

        private Date utilDate;

        private LocalDateTime localDateTime;
    }

}
