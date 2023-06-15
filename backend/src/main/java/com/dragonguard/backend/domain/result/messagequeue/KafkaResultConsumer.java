package com.dragonguard.backend.domain.result.messagequeue;

import com.dragonguard.backend.domain.result.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.result.dto.kafka.ResultDetailsResponse;
import com.dragonguard.backend.domain.result.dto.kafka.ResultKafkaResponse;
import com.dragonguard.backend.domain.result.dto.kafka.SearchKafkaResponse;
import com.dragonguard.backend.domain.result.service.ResultService;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 검색 결과를 처리하는 kafka consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaResultConsumer implements KafkaConsumer<ResultKafkaResponse> {

    private final ResultService resultService;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(topics = "gitrank.to.backend.result", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        ResultKafkaResponse resultResponse = readValue(message);

        List<GitRepoClientResponse> result = resultResponse.getResult().stream()
                .map(ResultDetailsResponse::getName)
                .map(GitRepoClientResponse::new)
                .collect(Collectors.toList());

        SearchKafkaResponse searchResponse = resultResponse.getSearch();

        SearchRequest searchRequest = new SearchRequest(searchResponse.getName(),
                SearchType.valueOf((searchResponse.getType()).toUpperCase()), searchResponse.getPage());

        resultService.saveAllResult(result, searchRequest);
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public ResultKafkaResponse readValue(String message) {
        return objectMapper.readValue(message, ResultKafkaResponse.class);
    }
}
