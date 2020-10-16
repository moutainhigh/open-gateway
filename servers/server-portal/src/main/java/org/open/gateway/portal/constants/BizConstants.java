package org.open.gateway.portal.constants;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
public interface BizConstants {

    String DEFAULT_OPERATE_PERSON = "portal";

    String TOKEN_PREFIX = "portal_token:";

    interface STATUS {

        byte NORMAL = 1;

        byte DISABLE = 0;

    }

    interface DEL_FLAG {

        // 已删除
        byte YES = 1;
        // 未删除
        byte NO = 0;

    }

}
