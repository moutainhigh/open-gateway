package open.gateway.common.utils;

import java.util.UUID;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
public class IdUtil {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}
