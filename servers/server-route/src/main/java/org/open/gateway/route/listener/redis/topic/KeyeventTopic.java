package org.open.gateway.route.listener.redis.topic;

import org.springframework.data.redis.listener.PatternTopic;

/**
 * Created by miko on 2020/6/11.
 *
 * @author MIKO
 */
public class KeyeventTopic extends PatternTopic {

    private static final String prefix = "__keyevent@0__:";

    public KeyeventTopic(String operate) {
        super(prefix + operate);
    }

}
