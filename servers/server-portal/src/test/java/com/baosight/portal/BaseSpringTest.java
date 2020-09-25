package com.baosight.portal;

import org.open.gateway.portal.GatewayPortalApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by miko on 2020/9/15.
 *
 * @author MIKO
 */
@SpringBootTest(
        classes = GatewayPortalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {"spring.profiles.active=dev"}
)
public class BaseSpringTest extends BaseTest {
}
