package org.open.gateway.route.listener.redis.topic;

import org.springframework.data.redis.listener.PatternTopic;

/**
 * Created by miko on 2020/6/11.
 *
 * @author MIKO
 */
public class KeyspaceTopic extends PatternTopic {

    private static final String PREFIX = "__keyspace@*__:";

    public KeyspaceTopic(String key) {
        super(PREFIX + key);
    }

}
