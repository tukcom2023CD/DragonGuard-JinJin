package com.dragonguard.backend.domain.blockchain.client;

import com.dragonguard.backend.config.blockchain.BlockchainProperties;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLException;

/**
 * @author 김승진
 * @description 24시간 마다 klaytn faucet 발급 api를 요청하는 클라이언트 클래스
 */
@Component
public class KlaytnFaucetClient {
    private static final String ADDRESS_PARAMETER = "address";
    private final BlockchainProperties blockchainProperties;
    private final WebClient webClient;

    public KlaytnFaucetClient(final BlockchainProperties blockchainProperties) throws SSLException {
        this.blockchainProperties = blockchainProperties;
        this.webClient = webClient();
    }

    public void requestKlaytnFaucet() {
        webClient
                .post()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .queryParam(
                                                ADDRESS_PARAMETER,
                                                blockchainProperties.getWalletAddress())
                                        .build())
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8);
    }

    private WebClient webClient() throws SSLException {
        final DefaultUriBuilderFactory factory =
                new DefaultUriBuilderFactory(blockchainProperties.getKlaytnApiUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        final SslContext sslContext =
                SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();

        final HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(blockchainProperties.getKlaytnApiUrl())
                .defaultHeader(HttpHeaders.USER_AGENT, blockchainProperties.getUserAgent())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
