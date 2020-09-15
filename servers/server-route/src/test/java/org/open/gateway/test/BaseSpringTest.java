package org.open.gateway.test;

import org.open.gateway.route.GatewayRouteApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by miko on 2020/9/15.
 *
 * @author MIKO
 */
@SpringBootTest(
        classes = GatewayRouteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {"spring.profiles.active=dev"}
)
public class BaseSpringTest extends BaseTest {
}
