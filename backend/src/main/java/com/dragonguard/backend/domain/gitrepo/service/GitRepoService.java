package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.Week;
import com.dragonguard.backend.domain.gitrepo.dto.collection.GitRepoContributionMap;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoInfoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;

public interface GitRepoService {
    TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest request);
    TwoGitRepoResponse findTwoGitReposAndUpdate(final GitRepoCompareRequest request);
    GitRepo findEntityByName(String name);
    void saveAll(final Set<GitRepo> gitRepos);
    boolean gitRepoExistsByName(final String name);
    GitRepo findGitRepo(String name);
    Optional<List<GitRepoMemberClientResponse>> requestClientGitRepoMember(GitRepoInfoRequest gitRepoInfoRequest);
    GitRepoContributionMap getContributionMap(Set<GitRepoMemberClientResponse> contributions, ToIntFunction<Week> function);
    void requestKafkaGitRepoInfo(String githubToken, String name);
    List<Integer> updateAndGetSparkLine(String name, String githubToken, GitRepo gitRepo);
}
