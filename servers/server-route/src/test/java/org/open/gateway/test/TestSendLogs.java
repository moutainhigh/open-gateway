package org.open.gateway.test;

import open.gateway.common.base.entity.AccessLogs;
import open.gateway.common.utils.StringUtil;
import org.junit.jupiter.api.Test;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;

import java.util.Date;

/**
 * Created by miko on 2020/8/20.
 *
 * @author MIKO
 */
public class TestSendLogs extends BaseSpringTest {

    @Autowired
    private AccessLogsService accessLogsService;

    @Test
    public void testSendAccessLogs() {
        StopWatch sw = newStopWatch();
        for (int i = 0; i < 10000; i++) {
            sw.start();
            AccessLogs accessLogs = new AccessLogs();
            accessLogs.setPath("/test/junit");
            accessLogs.setIp("127.0.0.1");
            accessLogs.setHttpStatus(HttpStatus.OK.value());
            accessLogs.setHttpMethod("POST");
            accessLogs.setHttpHeaders("Authorization: Bearer " + StringUtil.randomLetter(6) +
                    "Content-Type: application/x-www-form-urlencoded");
            accessLogs.setRequestQueryString(null);
            accessLogs.setRequestBody("packs=" + StringUtil.randomNumber(6));
            accessLogs.setRequestTime(new Date());
            accessLogs.setResponseTime(new Date());
            accessLogs.setUsedTime(100);
            accessLogs.setUserAgent("Postman");
            accessLogs.setRegion(null);
            accessLogs.setError(null);
            accessLogs.setApiCode(StringUtil.randomLetter(4));
            accessLogs.setRouteCode(StringUtil.randomLetter(4));
            accessLogsService.sendAccessLogs(accessLogs);
            sw.stop();
        }
        printSummary("sendAccessLogs", sw);
    }
}
