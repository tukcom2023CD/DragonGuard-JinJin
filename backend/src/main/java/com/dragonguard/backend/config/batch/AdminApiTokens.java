package com.dragonguard.backend.config.batch;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 배치처리에 필요한 API 토큰을 설정하는 클래스
 */

@Component
@RequiredArgsConstructor
public class AdminApiTokens {
    private final MemberQueryRepository memberQueryRepository;
    @Value("#{'${admin}'.split(',')}")
    private List<String> admins;
    private List<String> apiTokens;
    private Integer index = 0;

    @PostConstruct
    public void init() {
        this.apiTokens = admins.stream()
                .map(memberQueryRepository::findByGithubId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Member::getGithubToken)
                .collect(Collectors.toList());
    }


    public String getApiToken() {
        if (index < apiTokens.size()) {
            return apiTokens.get(index++);
        }
        index = 0;
        return apiTokens.get(index++);
    }
}
