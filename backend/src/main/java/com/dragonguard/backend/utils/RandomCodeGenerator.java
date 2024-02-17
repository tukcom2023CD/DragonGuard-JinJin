package com.dragonguard.backend.utils;

import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 랜덤 인증 코드 생성에 사용될 util 클래스
 */
@Component
public class RandomCodeGenerator {
    private static final int MIN = 10000;
    private static final int MAX = 99999;
    private final Random random;

    public RandomCodeGenerator() {
        this.random = new Random();
    }

    public int generate() {
        return random.nextInt(MAX - MIN) + MIN;
    }
}
