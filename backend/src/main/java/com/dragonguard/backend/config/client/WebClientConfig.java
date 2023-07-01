package com.dragonguard.backend.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
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
    private static final String USER_AGENT = "GITRANK WEB CLIENT";
    private final ObjectMapper objectMapper;

    @Bean
    public WebClient webClient(
            @Value("${github.url}") String url,
            @Value("${github.version-key}") String versionKey,
            @Value("${github.version-value}") String versionValue) throws SSLException {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(-1);
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(20L))
                .secure(t -> t.sslContext(sslContext));

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_API_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(versionKey, versionValue)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
