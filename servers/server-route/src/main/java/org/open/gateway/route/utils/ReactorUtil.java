package org.open.gateway.route.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/27.
 *
 * @author MIKO
 */
@Slf4j
public class ReactorUtil {

    public static <T> Mono<T> recordUsedTime(String method, Mono<T> mono) {
        StopWatch sw = new StopWatch();
        return mono.doOnSubscribe(sub -> sw.start())
                .doOnTerminate(() -> {
                    sw.stop();
                    log.info("方法:{} 耗时:{}", method, sw.getLastTaskInfo().getTimeMillis());
                });
    }

}
