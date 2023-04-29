package com.dragonguard.backend.commit.service;

import com.dragonguard.backend.contribution.dto.request.CommitScrapingRequest;
import com.dragonguard.backend.contribution.dto.response.CommitScrapingResponse;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.mapper.CommitMapper;
import com.dragonguard.backend.commit.repository.CommitRepository;
import com.dragonguard.backend.util.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommitService {

    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;

    public void saveCommits(CommitScrapingResponse commitScrapingResponse) {
        List<Commit> commits
                = commitRepository.findCommitsByGithubId(commitScrapingResponse.getGithubId());
        Commit commit = commitMapper.toEntity(commitScrapingResponse);

        if (commits.isEmpty()) {
            commitRepository.save(commit);
            return;
        }
        commits.stream()
                .filter(c -> !c.equals(commit))
                .findFirst()
                .ifPresent(commitRepository::save);
    }

    public List<Commit> findCommits(String githubId) {
        return commitRepository.findCommitsByGithubId(githubId);
    }
}
