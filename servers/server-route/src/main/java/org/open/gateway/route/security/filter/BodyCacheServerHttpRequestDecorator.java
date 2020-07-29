package org.open.gateway.route.security.filter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR;

@Slf4j
public class BodyCacheServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private boolean firstSubscribe = true;
    private final ServerWebExchange exchange;

    public BodyCacheServerHttpRequestDecorator(ServerWebExchange exchange) {
        super(exchange.getRequest());
        this.exchange = exchange;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        if (firstSubscribe) {
            final List<byte[]> listByteArray = new ArrayList<>();
            return getDelegate().getBody().map(dataBuffer -> {
                firstSubscribe = false;
                ByteBuf copy = ((NettyDataBuffer) dataBuffer).getNativeBuffer().copy();
                byte[] dst = new byte[copy.readableBytes()];
                copy.readBytes(dst);
                listByteArray.add(dst);
                copy.release();
                exchange.getAttributes().put(CACHED_REQUEST_BODY_ATTR, Flux.fromIterable(listByteArray).map(this::getDataBuffer));
                return dataBuffer;
            });
        } else {
            return exchange.getAttribute(CACHED_REQUEST_BODY_ATTR);
        }
    }

    private DataBuffer getDataBuffer(byte[] bytes) {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        return nettyDataBufferFactory.wrap(bytes);
    }
}