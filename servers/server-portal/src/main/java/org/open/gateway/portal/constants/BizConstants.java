package org.open.gateway.portal.constants;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
public interface BizConstants {

    String ROOT_CODE = "ROOT";

    /**
     * 状态
     */
    interface STATUS {

        // 正常
        byte NORMAL = 1;

        // 禁用
        byte DISABLE = 0;

    }

    /**
     * 删除标记
     */
    interface DEL_FLAG {

        // 已删除
        byte YES = 1;
        // 未删除
        byte NO = 0;

    }

}
