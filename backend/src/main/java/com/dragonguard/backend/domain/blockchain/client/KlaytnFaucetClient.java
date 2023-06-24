package com.dragonguard.backend.domain.blockchain.client;

import com.dragonguard.backend.config.blockchain.BlockchainProperties;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class KlaytnFaucetClient {
    private final BlockchainProperties blockchainProperties;

    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    public void requestKlaytnFaucet() throws SSLException {
        webClient().post()
                .uri(
                        uriBuilder -> uriBuilder
                                .queryParam("address", blockchainProperties.getWalletAddress())
                                .build())
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8);
    }

    public WebClient webClient() throws SSLException {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(blockchainProperties.getKlaytnApiUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(t -> t.sslContext(sslContext));

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(blockchainProperties.getKlaytnApiUrl())
                .defaultHeader(HttpHeaders.USER_AGENT, blockchainProperties.getUserAgent())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
