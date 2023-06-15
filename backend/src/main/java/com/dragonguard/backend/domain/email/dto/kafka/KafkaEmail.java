package com.dragonguard.backend.domain.email.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEmail {
    private String memberEmail;
    private int random;
}
