package com.dragonguard.backend.domain.member.entity;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganizationMember;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버 정보를 담는 DB Entity
 */

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    @Column(nullable = false, unique = true)
    private String githubId;

    private String profileImage;

    private String walletAddress;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Enumerated(EnumType.STRING)
    private AuthStep authStep;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private List<PullRequest> pullRequests = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private List<Blockchain> blockchains = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private List<GitOrganizationMember> gitOrganizationMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<GitRepoMember> gitRepoMembers = new ArrayList<>();

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> role = new ArrayList<>(List.of(Role.ROLE_USER));

    private String refreshToken;

    private String githubToken;

    private String emailAddress;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Formula("(SELECT COALESCE(sum(c.amount), 0) FROM commit c WHERE c.member_id = id)")
    private Integer sumOfCommits;

    @Formula("(SELECT COALESCE(sum(i.amount), 0) FROM issue i WHERE i.member_id = id)")
    private Integer sumOfIssues;

    @Formula("(SELECT COALESCE(sum(pr.amount), 0) FROM pull_request pr WHERE pr.member_id = id)")
    private Integer sumOfPullRequests;

    @Formula("(SELECT COALESCE(sum(b.amount), 0) FROM blockchain b WHERE b.member_id = id)")
    private Long sumOfTokens;

    private Integer sumOfReviews;

    @Builder
    public Member(String name, String githubId, String walletAddress, String profileImage, Role role, AuthStep authStep) {
        this.name = name;
        this.githubId = githubId;
        this.walletAddress = walletAddress;
        this.profileImage = profileImage;
        this.tier = Tier.SPROUT;
        this.authStep = authStep;
        if (role != null && role.equals(Role.ROLE_ADMIN)) {
            this.role.add(role);
        }
    }

    public void addCommit(Commit commit) {
        if (commit == null || this.commits.stream().anyMatch(commit::customEqualsWithAmount)) return;
        if (this.commits.stream().anyMatch(commit::customEquals)) {
            this.commits.stream().filter(commit::customEquals).findFirst().ifPresent(c -> {
                this.commits.remove(c);
                c.delete();
            });
        }
        this.commits.add(commit);
    }

    public void addIssue(Issue issue) {
        if (issue == null || this.issues.stream().anyMatch(issue::customEqualsWithAmount)) return;
        if (this.issues.stream().anyMatch(issue::customEquals)) {
            this.issues.stream().filter(issue::customEquals).findFirst().ifPresent(i -> {
                this.issues.remove(i);
                i.delete();
            });
        }
        this.issues.add(issue);
    }

    public void addPullRequest(PullRequest pullRequest) {
        if (pullRequest == null || this.pullRequests.stream().anyMatch(pullRequest::customEqualsWithAmount)) return;
        if (this.pullRequests.stream().anyMatch(pullRequest::customEquals)) {
            this.pullRequests.stream().filter(pullRequest::customEquals).findFirst().ifPresent(p -> {
                this.pullRequests.remove(p);
                p.delete();
            });
        }
        this.pullRequests.add(pullRequest);
    }

    public void updateNameAndImage(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateTier() {
        if (sumOfTokens != null) {
            this.tier = checkTier(sumOfTokens);
            return;
        }
        if (blockchains.isEmpty()) {
            this.tier = checkTier(0L);
            return;
        }

        long amount = this.blockchains.stream()
                .map(Blockchain::getAmount)
                .mapToLong(b -> Long.parseLong(b.toString()))
                .sum();

        if (amount < 0) deleteContributions();

        this.tier = checkTier(amount);
    }

    public Tier checkTier(long amount) {
        return Arrays.stream(Tier.values())
                .filter(t -> t.getTierPredicate().test(amount))
                .findFirst()
                .orElse(Tier.SPROUT);
    }

    public void updateWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
        this.authStep = AuthStep.GITHUB_AND_KLIP;
    }

    public List<SimpleGrantedAuthority> getRole() {
        return role.stream().map(Role::name).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public void updateGithubToken(String githubToken) {
        this.githubToken = githubToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateOrganization(Organization organization, String emailAddress) {
        this.organization = organization;
        this.emailAddress = emailAddress;
    }

    public void organizeGitOrganizationMember(GitOrganizationMember gitOrganizationMember) {
        this.gitOrganizationMembers.add(gitOrganizationMember);
    }

    public void organizeGitRepoMember(GitRepoMember gitRepoMember) {
        this.gitRepoMembers.add(gitRepoMember);
    }

    public void finishAuth() {
        this.authStep = AuthStep.ALL;
    }

    public void updateSumOfReviews(Integer sumOfReviews) {
        if (sumOfReviews < 0) return;
        this.sumOfReviews = sumOfReviews;
    }

    public int getCommitSumWithRelation() {
        return this.commits.stream().mapToInt(Commit::getAmount).sum();
    }

    public int getIssueSumWithRelation() {
        return this.issues.stream().mapToInt(Issue::getAmount).sum();
    }

    public int getPullRequestSumWithRelation() {
        return this.pullRequests.stream().mapToInt(PullRequest::getAmount).sum();
    }

    public boolean isWalletAddressExists() {
        return StringUtils.hasText(this.getWalletAddress());
    }

    public String getBlockchainUrl() {
        if (!isWalletAddressExists()) {
            return null;
        }
        return "https://baobab.scope.klaytn.com/account/" + this.walletAddress + "?tabId=txList";
    }

    public String getGithubToken() {
        if (StringUtils.hasText(this.githubToken)) {
            return this.githubToken;
        }
        return "";
    }

    public Optional<Integer> getSumOfReviews() {
        return Optional.ofNullable(sumOfReviews);
    }

    public Optional<Integer> getSumOfCommits() {
        return Optional.ofNullable(sumOfCommits);
    }

    public Optional<Integer> getSumOfIssues() {
        return Optional.ofNullable(sumOfIssues);
    }

    public Optional<Integer> getSumOfPullRequests() {
        return Optional.ofNullable(sumOfPullRequests);
    }

    private void deleteContributions() {
        this.commits.forEach(Commit::delete);
        this.pullRequests.forEach(PullRequest::delete);
        this.issues.forEach(Issue::delete);
        this.blockchains.forEach(Blockchain::delete);
        this.sumOfReviews = 0;
        this.tier = Tier.SPROUT;
    }

    public boolean validateWalletAddressAndUpdateTier() {
        if (!isWalletAddressExists()) return true;
        updateTier();
        return false;
    }

    public void organizeBlockchain(Blockchain blockchain) {
        this.blockchains.add(blockchain);
        updateTier();
    }

    public boolean isServiceMember() {
        return !this.authStep.equals(AuthStep.NONE);
    }

    public void updateAuthStepAndNameAndProfileImage(AuthStep authStep, String name, String profileImage) {
        this.authStep = authStep;
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateSumOfReviewsWithCalculation(Integer contribution) {
        int reviews = contribution - getContributionSumWithoutReviews();
        if (reviews < 0) {
            deleteContributions();
            return;
        }
        this.sumOfReviews = reviews;
    }

    public Integer getContributionSumWithoutReviews() {
        if (this.commits.isEmpty() || this.issues.isEmpty() || this.pullRequests.isEmpty()) return -1;
        return getCommitSumWithRelation() + getPullRequestSumWithRelation() + getIssueSumWithRelation();
    }
}
