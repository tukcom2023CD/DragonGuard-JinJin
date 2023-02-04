package com.dragonguard.backend.commit.service;

import com.dragonguard.backend.commit.dto.request.CommitScrappingRequest;
import com.dragonguard.backend.commit.dto.response.CommitScrappingResponse;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.commit.mapper.CommitMapper;
import com.dragonguard.backend.commit.repository.CommitRepository;
import com.dragonguard.backend.global.webclient.ScrappingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommitService {

    private final CommitRepository commitRepository;
    private final CommitMapper commitMapper;
    private final ScrappingClient<CommitScrappingRequest> scrappingClient;

    public void scrappingCommits(String githubId) {
        scrappingClient.requestToScrapping(commitMapper.toRequest(githubId));
    }

    public void saveCommit(CommitScrappingResponse commitScrappingResponse) {
        List<Commit> commits
                = commitRepository.findCommitsByGithubId(commitScrappingResponse.getGithubId());
        Commit commit = commitMapper.toEntity(commitScrappingResponse);
        if (commits.isEmpty()) {
            commitRepository.save(commit);
        } else {
            commits.stream()
                    .filter(c -> !c.equals(commit))
                    .findFirst()
                    .ifPresent(com -> commitRepository.save(com));
        }
    }

    public void saveAllCommits(List<Commit> commits) {
        commitRepository.saveAll(commits);
    }

    public List<Commit> findCommits(String githubId) {
        return commitRepository.findCommitsByGithubId(githubId);
    }
}
