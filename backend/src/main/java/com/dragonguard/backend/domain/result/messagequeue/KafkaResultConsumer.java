package com.dragonguard.backend.domain.result.messagequeue;

import com.dragonguard.backend.domain.result.dto.response.client.ClientResultResponse;
import com.dragonguard.backend.domain.result.service.ResultService;
import com.dragonguard.backend.domain.search.dto.request.SearchRequest;
import com.dragonguard.backend.domain.search.entity.SearchType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 검색 결과를 처리하는 kafka consumer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaResultConsumer {

    private final ResultService resultService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "gitrank.to.backend.result", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<Object, Object> map = null;

        try {
            map = objectMapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {}

        if (Objects.isNull(map)) return;

        List<Map<String, String>> results = (List<Map<String, String>>) map.get("result");
        List<ClientResultResponse> result = results.stream()
                .map(o -> new ClientResultResponse(o.get("name")))
                .collect(Collectors.toList());

        Map<String, Object> resultMap = (Map<String, Object>) map.get("search");

        SearchRequest searchRequest = new SearchRequest((String) resultMap.get("name"),
                SearchType.valueOf(((String) resultMap.get("type")).toUpperCase()),
                (Integer) resultMap.get("page"));

        resultService.saveAllResult(result, searchRequest);
    }
}
