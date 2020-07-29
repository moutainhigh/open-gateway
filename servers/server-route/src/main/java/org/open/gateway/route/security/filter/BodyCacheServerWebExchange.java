package org.open.gateway.route.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;

@Slf4j
public class BodyCacheServerWebExchange extends ServerWebExchangeDecorator {

    private static final ResolvableType FORM_DATA_TYPE =
            ResolvableType.forClassWithGenerics(MultiValueMap.class, String.class, String.class);

    private static final Mono<MultiValueMap<String, String>> EMPTY_FORM_DATA =
            Mono.just(CollectionUtils.unmodifiableMultiValueMap(new LinkedMultiValueMap<String, String>(0)))
                    .cache();

    private final ServerHttpRequest request;
    private final Mono<MultiValueMap<String, String>> formDataMono;

    public BodyCacheServerWebExchange(ServerWebExchange delegate, ServerCodecConfigurer configurer) {
        super(delegate);
        this.request = new BodyCacheServerHttpRequestDecorator(delegate);
        this.formDataMono = initFormData(this.request, configurer, getLogPrefix());
    }

    @Override
    public ServerHttpRequest getRequest() {
        return this.request;
    }

    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        return this.formDataMono;
    }

    @SuppressWarnings("unchecked")
    private Mono<MultiValueMap<String, String>> initFormData(ServerHttpRequest request, ServerCodecConfigurer configurer, String logPrefix) {
        MediaType contentType = request.getHeaders().getContentType();
        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
            return ((HttpMessageReader<MultiValueMap<String, String>>) configurer.getReaders().stream()
                    .filter(reader -> reader.canRead(FORM_DATA_TYPE, MediaType.APPLICATION_FORM_URLENCODED))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No form data HttpMessageReader.")))
                    .readMono(FORM_DATA_TYPE, request, Hints.from(Hints.LOG_PREFIX_HINT, logPrefix))
                    .switchIfEmpty(EMPTY_FORM_DATA)
                    .cache();
        }
        return getDelegate().getFormData();
    }
}