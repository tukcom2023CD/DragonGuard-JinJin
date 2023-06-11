package com.dragonguard.backend.domain.gitrepo.service;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientResponse;
import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.dto.kafka.ClosedIssueKafkaResponse;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoCompareRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoNameRequest;
import com.dragonguard.backend.domain.gitrepo.dto.request.GitRepoRequest;
import com.dragonguard.backend.domain.gitrepo.dto.response.GitRepoMemberCompareResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.StatisticsResponse;
import com.dragonguard.backend.domain.gitrepo.dto.response.TwoGitRepoResponse;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepoContributionMap;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepoLanguageMap;
import com.dragonguard.backend.domain.gitrepo.mapper.GitRepoMapper;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.dto.client.GitRepoMemberClientResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.request.GitRepoMemberCompareRequest;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.TwoGitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.dto.response.Week;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.mapper.GitRepoMemberMapper;
import com.dragonguard.backend.domain.gitrepomember.service.GitRepoMemberService;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.KafkaProducer;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.service.EntityLoader;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 서비스 로직을 처리하는 클래스
 */

@TransactionService
@RequiredArgsConstructor
public class GitRepoService implements EntityLoader<GitRepo, Long> {
    private final GitRepoMemberMapper gitRepoMemberMapper;
    private final GitRepoRepository gitRepoRepository;
    private final GitRepoMemberService gitRepoMemberService;
    private final MemberService memberService;
    private final GitRepoMapper gitRepoMapper;
    private final KafkaProducer<GitRepoNameRequest> kafkaIssueProducer;
    private final GithubClient<GitRepoRequest, GitRepoMemberClientResponse[]> gitRepoMemberClient;
    private final GithubClient<GitRepoClientRequest, GitRepoClientResponse> gitRepoClient;
    private final GithubClient<GitRepoClientRequest, Map<String, Integer>> gitRepoLanguageClient;

    public List<GitRepoMemberResponse> findMembersByGitRepoWithClient(final GitRepoRequest gitRepoRequest) {
        Optional<GitRepo> gitRepo = findByName(gitRepoRequest);

        if (isGithubTokenValid(gitRepoRequest)) gitRepoRequest.setGithubToken(memberService.getLoginUserWithPersistence().getGithubToken());
        if (checkGitRepoIfValidAndSave(gitRepoRequest, gitRepo)) return requestToGithub(gitRepoRequest);
        if (isContributionValid(gitRepo.get().getGitRepoMembers())) return requestToGithub(gitRepoRequest);

        return getGitRepoMemberResponses(gitRepo);
    }

    private Optional<GitRepo> findByName(final GitRepoRequest gitRepoRequest) {
        return gitRepoRepository.findByName(gitRepoRequest.getName());
    }

    public boolean checkGitRepoIfValidAndSave(final GitRepoRequest gitRepoRequest, final Optional<GitRepo> gitRepo) {
        if (gitRepo.isEmpty()) {
            saveGitRepo(gitRepoRequest);
            return true;
        }
        return false;
    }

    private boolean isContributionValid(final Set<GitRepoMember> gitRepoMembers) {
        return gitRepoMembers.isEmpty() || gitRepoMembers.stream().findFirst().get().getGitRepoContribution() == null;
    }

    private boolean isGithubTokenValid(final GitRepoRequest gitRepoRequest) {
        return !StringUtils.hasText(gitRepoRequest.getGithubToken());
    }

    private List<GitRepoMemberResponse> getGitRepoMemberResponses(final Optional<GitRepo> gitRepo) {
        return gitRepo.map(gitRepoMemberService::findAllByGitRepo).orElseGet(List::of).stream()
                .map(gitRepoMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void saveGitRepo(final GitRepoRequest gitRepoRequest) {
        try {
            gitRepoRepository.save(gitRepoMapper.toEntity(gitRepoRequest.getName()));
        } catch (DataIntegrityViolationException e) {}
    }

    public TwoGitRepoMemberResponse findMembersByGitRepoForCompare(final GitRepoCompareRequest gitRepoCompareRequest) {
        return getTwoGitRepoMemberResponse(gitRepoCompareRequest,
                LocalDate.now().getYear(), memberService.getLoginUserWithPersistence().getGithubToken());
    }

    private TwoGitRepoMemberResponse getTwoGitRepoMemberResponse(final GitRepoCompareRequest gitRepoCompareRequest, final Integer year, final String githubToken) {
        return new TwoGitRepoMemberResponse(getGitRepoMemberResponses(gitRepoCompareRequest.getFirstRepo(), year, githubToken),
                getGitRepoMemberResponses(gitRepoCompareRequest.getSecondRepo(), year, githubToken));
    }

    private List<GitRepoMemberResponse> getGitRepoMemberResponses(final String repo, final Integer year, final String githubToken) {
        requestIssueToScraping(new GitRepoNameRequest(repo));
        return findMembersByGitRepoWithClient(new GitRepoRequest(githubToken, repo, year));
    }

    public GitRepoMemberCompareResponse findTwoGitRepoMember(final GitRepoMemberCompareRequest gitRepoMemberCompareRequest) {
        GitRepoMember firstGitRepoMember =
                gitRepoMemberService.findByNameAndMemberName(gitRepoMemberCompareRequest.getFirstRepo(), gitRepoMemberCompareRequest.getFirstName());
        GitRepoMember secondGitRepoMember =
                gitRepoMemberService.findByNameAndMemberName(gitRepoMemberCompareRequest.getSecondRepo(), gitRepoMemberCompareRequest.getSecondName());

        return new GitRepoMemberCompareResponse(
                gitRepoMemberMapper.toResponse(firstGitRepoMember),
                gitRepoMemberMapper.toResponse(secondGitRepoMember));
    }

    public TwoGitRepoResponse findTwoGitRepos(final GitRepoCompareRequest twoGitRepoCompareRequest) {
        return new TwoGitRepoResponse(getOneRepoResponse(twoGitRepoCompareRequest.getFirstRepo()),
                getOneRepoResponse(twoGitRepoCompareRequest.getSecondRepo()));
    }

    public void updateClosedIssues(final ClosedIssueKafkaResponse closedIssueKafkaResponse) {
        GitRepo gitRepo = gitRepoRepository.findByName(closedIssueKafkaResponse.getName())
                .orElseThrow(EntityNotFoundException::new);

        gitRepo.updateClosedIssueNum(closedIssueKafkaResponse.getClosedIssue());
    }

    private GitRepoResponse getOneRepoResponse(final String repoName) {
        String githubToken = memberService.getLoginUserWithPersistence().getGithubToken();
        GitRepoClientResponse repoResponse = gitRepoClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName));

        if (validateAndSetClosedIssue(findGitRepo(repoName), repoResponse)) {
            requestIssueToScraping(new GitRepoNameRequest(repoName));
        }

        return getGitRepoResponse(repoName, repoResponse, getGitRepoLanguage(repoName, githubToken));
    }

    private GitRepoResponse getGitRepoResponse(final String repoName, final GitRepoClientResponse repoResponse, final GitRepoLanguageMap gitRepoLanguageMap) {
        return new GitRepoResponse(repoResponse, getStatistics(repoName), gitRepoLanguageMap.getLanguages(), gitRepoLanguageMap.getStatistics());
    }

    private GitRepoLanguageMap getGitRepoLanguage(final String repoName, final String githubToken) {
        return new GitRepoLanguageMap(gitRepoLanguageClient.requestToGithub(new GitRepoClientRequest(githubToken, repoName)));
    }

    private boolean validateAndSetClosedIssue(final GitRepo gitRepo, final GitRepoClientResponse repoResponse) {
        if (isClosedIssueNotValid(gitRepo)) {
            repoResponse.setClosed_issues_count(gitRepo.getClosedIssueNum());
            return false;
        }
        return true;
    }

    private boolean isClosedIssueNotValid(final GitRepo gitRepo) {
        return gitRepo != null && gitRepo.getClosedIssueNum() != null;
    }

    public GitRepo findGitRepo(final String repoName) {
        try {
            return gitRepoRepository.findByName(repoName).orElseGet(() -> gitRepoRepository.save(gitRepoMapper.toEntity(repoName)));
        } catch (DataIntegrityViolationException e) {}
        return null;
    }

    private StatisticsResponse getStatistics(final String name) {
        GitRepo gitRepo = getEntityByName(name);
        return getContributionStatisticsResponse(gitRepo.getGitRepoMembers());
    }

    private StatisticsResponse getContributionStatisticsResponse(final Set<GitRepoMember> gitRepoMembers) {
        return getStatisticsResponse(
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getCommits()),
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getAdditions()),
                getContributionList(gitRepoMembers, gitRepoMember -> gitRepoMember.getGitRepoContribution().getDeletions()));
    }

    private List<Integer> getContributionList(final Set<GitRepoMember> gitRepoMembers, final Function<GitRepoMember, Integer> function) {
        return gitRepoMembers.stream().map(function).collect(Collectors.toList());
    }

    private StatisticsResponse getStatisticsResponse(final List<Integer> commits, final List<Integer> additions, final List<Integer> deletions) {
        return new StatisticsResponse(
                commits.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : commits.stream().mapToInt(Integer::intValue).summaryStatistics(),
                additions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : additions.stream().mapToInt(Integer::intValue).summaryStatistics(),
                deletions.isEmpty() ? new IntSummaryStatistics(0, 0, 0, 0) : deletions.stream().mapToInt(Integer::intValue).summaryStatistics());
    }

    private List<GitRepoMemberResponse> requestToGithub(final GitRepoRequest gitRepoRequest) {
        List<GitRepoMemberClientResponse> contributions = Arrays.asList(gitRepoMemberClient.requestToGithub(gitRepoRequest));
        if (contributions.isEmpty()) return List.of();

        List<GitRepoMemberResponse> result = getResponseList(
                contributions, getContributionMap(contributions, Week::getA), getContributionMap(contributions, Week::getD));

        gitRepoMemberService.saveAll(result, gitRepoRequest.getName());
        return result;
    }

    private List<GitRepoMemberResponse> getResponseList(
            final List<GitRepoMemberClientResponse> contributions,
            final GitRepoContributionMap additions,
            final GitRepoContributionMap deletions) {

        return contributions.stream()
                .map(clientResponse ->
                        new GitRepoMemberResponse(
                                clientResponse.getAuthor().getLogin(),
                                clientResponse.getTotal(),
                                additions.getContributionByKey(clientResponse),
                                deletions.getContributionByKey(clientResponse)))
                .collect(Collectors.toList());
    }

    private GitRepoContributionMap getContributionMap(final List<GitRepoMemberClientResponse> contributions, final ToIntFunction<Week> function) {
        return new GitRepoContributionMap(contributions.stream()
                .collect(Collectors.toMap(Function.identity(), mem -> Arrays.stream(mem.getWeeks()).mapToInt(function).sum())));
    }

    private GitRepo getEntityByName(final String name) {
        return gitRepoRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    private void requestIssueToScraping(final GitRepoNameRequest gitRepoNameRequest) {
        kafkaIssueProducer.send(gitRepoNameRequest);
    }

    @Override
    public GitRepo loadEntity(final Long id) {
        return gitRepoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
