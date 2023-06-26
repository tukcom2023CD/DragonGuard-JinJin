package com.dragonguard.backend.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 김승진
 * @description 배치처리에 필요한 API 토큰을 설정하는 클래스
 */

@Component
@RequiredArgsConstructor
public class AdminApiToken {
    @Value("#{'${admin-tokens}'.split(',')}")
    private List<String> adminTokens;
    private Integer index = 0;


    public synchronized String getApiToken() {
        if (index < adminTokens.size()) {
            return adminTokens.get(index++);
        }
        index = 0;
        return adminTokens.get(index++);
    }
}
