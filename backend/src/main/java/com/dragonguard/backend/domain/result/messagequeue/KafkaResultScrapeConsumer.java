package com.dragonguard.backend.domain.result.messagequeue;

import com.dragonguard.backend.domain.search.dto.kafka.ResultDetailsResponse;
import com.dragonguard.backend.domain.search.dto.kafka.ResultKafkaResponse;
import com.dragonguard.backend.domain.search.dto.kafka.ScrapeResult;
import com.dragonguard.backend.domain.search.dto.kafka.SearchKafkaResponse;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.dragonguard.backend.domain.search.service.SearchResultFacade;
import com.dragonguard.backend.global.template.kafka.KafkaConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 검색 결과를 처리하는 kafka consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaResultScrapeConsumer implements KafkaConsumer<ResultKafkaResponse> {
    private final SearchResultFacade resultService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @KafkaListener(topics = "gitrank.to.backend.result", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final String message) {
        ResultKafkaResponse resultResponse = readValue(message);

        List<ScrapeResult> result = resultResponse.getResult().stream()
                .map(ResultDetailsResponse::getName)
                .map(ScrapeResult::new)
                .collect(Collectors.toList());

        SearchKafkaResponse searchResponse = resultResponse.getSearch();

        SearchRequest searchRequest = new SearchRequest(searchResponse.getName(),
                SearchType.valueOf((searchResponse.getType()).toUpperCase()), searchResponse.getPage());

        resultService.saveAllResult(result, searchRequest);
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public ResultKafkaResponse readValue(final String message) {
        return objectMapper.readValue(message, ResultKafkaResponse.class);
    }
}
