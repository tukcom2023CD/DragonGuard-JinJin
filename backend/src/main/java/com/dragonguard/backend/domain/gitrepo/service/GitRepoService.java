package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.Week;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoContributions;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.global.template.service.EntityLoader;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;

public interface GitRepoService extends EntityLoader<GitRepo, Long> {
    TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request);

    TwoGitRepoResponse findTwoGitReposAndUpdate(final GitRepoCompareRequest request);

    void saveAllIfNotExists(final Set<String> gitRepos);

    boolean gitRepoExistsByName(final String name);

    GitRepo findGitRepo(final String name);

    Optional<List<GitRepoMemberClientResponse>> requestClientGitRepoMember(
            final GitRepoInfoRequest gitRepoInfoRequest);

    GitRepoContributions getContributionMap(
            final Set<GitRepoMemberClientResponse> contributions,
            final ToIntFunction<Week> function);

    void requestKafkaGitRepoInfo(final String githubToken, final String name);

    List<Integer> updateAndGetSparkLine(
            final String name, final String githubToken, final GitRepo gitRepo);

    void updateSparkLine(final Long id, final String githubToken);
}
