package com.dragonguard.backend.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.Duration;

/**
 * @author 김승진
 * @description Github로 요청을 보내는 WebClient를 Bean으로 등록하는 설정 클래스
 */

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private static final String GITHUB_API_MIME_TYPE = "application/vnd.github+json";
    private static final String USER_AGENT = "request";
    private static final long REQUEST_TIMEOUT_DURATION = 20L;
    private final ObjectMapper objectMapper;

    @Bean
    public WebClient webClient(
            @Value("${github.url}") final String url,
            @Value("${github.version-key}") final String versionKey,
            @Value("${github.version-value}") final String versionValue) throws SSLException {
        return WebClient.builder()
                .uriBuilderFactory(getDefaultUriBuilderFactory(url))
                .exchangeStrategies(getExchangeStrategies())
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_API_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(versionKey, versionValue)
                .clientConnector(getReactorClientHttpConnector())
                .build();
    }

    private ReactorClientHttpConnector getReactorClientHttpConnector() throws SSLException {
        return new ReactorClientHttpConnector(getHttpClient(getSslContext()));
    }

    private ExchangeStrategies getExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(-1);
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                }).build();
    }

    private DefaultUriBuilderFactory getDefaultUriBuilderFactory(final String url) {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return factory;
    }

    private SslContext getSslContext() throws SSLException {
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
    }

    private HttpClient getHttpClient(final SslContext sslContext) {
        return HttpClient.create()
                .responseTimeout(Duration.ofSeconds(REQUEST_TIMEOUT_DURATION))
                .secure(t -> t.sslContext(sslContext));
    }
}
