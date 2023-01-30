package com.dragonguard.backend.result.messagequeue;

import com.dragonguard.backend.result.dto.request.ResultRequest;
import com.dragonguard.backend.result.service.ResultService;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.entity.SearchType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaResultConsumer {

    private final ResultService resultService;

    @KafkaListener(topics = "gitrank.to.backend.result", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (map.isEmpty()) {
            return;
        }

        List<Map<String, String>> results = (List<Map<String, String>>) map.get("result");
        List<ResultRequest> result = results.stream()
                .map(o -> new ResultRequest(o.get("name")))
                .collect(Collectors.toList());

        Map<String, Object> getMap = (Map<String, Object>) map.get("search");
        SearchRequest searchRequest = new SearchRequest((String) getMap.get("name"),
                SearchType.valueOf(((String) getMap.get("type")).toUpperCase()), Integer.parseInt((String) getMap.get("page")));

        resultService.saveAllResult(result, searchRequest);
    }
}
