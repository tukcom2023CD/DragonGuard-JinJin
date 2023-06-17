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

@Component
@RequiredArgsConstructor
public class AdminApiKeys {
    private final MemberQueryRepository memberQueryRepository;
    @Value("#{'${admin}'.split(',')}")
    private List<String> admins;
    private List<String> apiTokens;
    private Integer index = 0;

    @PostConstruct
    public void init() {
        this.apiTokens = admins.stream()
                .map(memberQueryRepository::findByGithubId)
                .map(Optional::orElseThrow)
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
