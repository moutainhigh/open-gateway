package org.open.gateway.route.security.filter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BodyCacheServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private boolean firstSubscribe = true;
    private final List<byte[]> listByteArray = new ArrayList<>();

    public BodyCacheServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        if (firstSubscribe) {
            return getDelegate().getBody().map(dataBuffer -> {
                firstSubscribe = false;
                ByteBuf copy = ((NettyDataBuffer) dataBuffer).getNativeBuffer().copy();
                byte[] dst = new byte[copy.readableBytes()];
                copy.readBytes(dst);
                listByteArray.add(dst);
                copy.release();
                return dataBuffer;
            });
        } else {
            return Flux.fromIterable(listByteArray).map(this::getDataBuffer);
        }
    }

    private DataBuffer getDataBuffer(byte[] bytes) {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        return nettyDataBufferFactory.wrap(bytes);
    }
}