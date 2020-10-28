package org.open.gateway.portal.constants;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
public interface BizConstants {

    /**
     * 根资源代码
     */
    String RESOURCE_ROOT_CODE = "ROOT";

    /**
     * 状态
     */
    interface STATUS {

        // 启用
        byte ENABLE = 1;

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

    /**
     * 资源类型(D目录 M菜单 B按钮)
     */
    interface RESOURCE_TYPE {

        // 目录
        String DIRECTORY = "D";

        // 菜单
        String MENU = "M";

        // 按钮
        String BUTTON = "B";

    }

}
