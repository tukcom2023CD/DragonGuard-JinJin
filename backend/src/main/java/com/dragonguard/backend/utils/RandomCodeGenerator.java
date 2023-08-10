package com.dragonguard.backend.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCodeGenerator {
    private static final int MIN = 10000;
    private static final int MAX = 99999;
    private static final Random RANDOM = new Random();

    public int generate() {
        return RANDOM.nextInt(MAX - MIN) + MIN;
    }
}
