package org.open.gateway.route.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/27.
 *
 * @author MIKO
 */
@Slf4j
public class ReactorUtil {

    /**
     * 记录耗时时间
     *
     * @param prefix 前缀
     * @param mono   执行逻辑
     * @return 包装后的执行逻辑
     */
    public static <T> Mono<T> recordUsedTime(String prefix, Mono<T> mono) {
        StopWatch sw = new StopWatch();
        return mono.doOnSubscribe(sub -> sw.start())
                .doOnTerminate(() -> {
                    sw.stop();
                    log.info("{} execute finished. spend:[{}ms.][{}ns.]", prefix, sw.getTotalTimeMillis(), sw.getTotalTimeNanos());
                });
    }

    /**
     * 记录耗时时间
     *
     * @param prefix 前缀
     * @param flux   执行逻辑
     * @return 包装后的执行逻辑
     */
    public static <T> Flux<T> recordUsedTime(String prefix, Flux<T> flux) {
        StopWatch sw = new StopWatch();
        return flux.doOnSubscribe(sub -> sw.start())
                .doOnTerminate(() -> {
                    sw.stop();
                    log.info("{} execute finished. spend:[{}ms.][{}ns.]", prefix, sw.getTotalTimeMillis(), sw.getTotalTimeNanos());
                });
    }

}
