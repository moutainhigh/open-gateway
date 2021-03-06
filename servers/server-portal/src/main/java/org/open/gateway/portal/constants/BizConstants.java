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
     * 默认的排序
     */
    int DEFAULT_SORT = 1;

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

    /**
     * 注册来源
     */
    interface REGISTER_FROM {

        /**
         * 前端页面
         */
        String FRONT = "front";

        /**
         * 管理后台
         */
        String PORTAL = "portal";

    }

    /**
     * 路由类型
     */
    interface ROUTE_TYPE {

        byte HTTP = 1;

        byte DUBBO = 0;

    }

}
