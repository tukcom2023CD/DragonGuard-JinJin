package com.dragonguard.backend.domain.member.entity;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.codereview.entity.CodeReview;
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
import org.hibernate.annotations.Where;
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
@Where(clause = "deleted_at is null")
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "member")
    private Commit commit;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "member")
    private Issue issue;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "member")
    private PullRequest pullRequest;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "member")
    private CodeReview codeReview;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private List<Blockchain> blockchains = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "member")
    private List<GitOrganizationMember> gitOrganizationMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<GitRepoMember> gitRepoMembers = new ArrayList<>();

    @JoinColumn
    @Where(clause = "organization_status = 'ACCEPTED'")
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

    @Formula("(SELECT COALESCE(sum(cr.amount), 0) FROM code_review cr WHERE cr.member_id = id)")
    private Integer sumOfCodeReviews;

    @Formula("(SELECT COALESCE(sum(h.amount), 0) FROM member m left join blockchain b on b.member_id = m.id left join history h on h.blockchain_id = b.id where m.id = id)")
    private Long sumOfTokens;

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
        if (commit == null || (this.commit != null && this.commit.customEqualsWithAmount(commit))) return;
        this.commit = commit;
    }

    public void addIssue(Issue issue) {
        if (issue == null || (this.issue != null && this.issue.customEqualsWithAmount(issue))) return;
        this.issue = issue;
    }

    public void addPullRequest(PullRequest pullRequest) {
        if (pullRequest == null || (this.pullRequest != null && this.pullRequest.customEqualsWithAmount(pullRequest))) return;
        this.pullRequest = pullRequest;
    }

    public void addCodeReview(CodeReview codeReview) {
        if (codeReview == null || (this.codeReview != null && this.codeReview.customEqualsWithAmount(codeReview))) return;
        this.codeReview = codeReview;
    }

    public void updateNameAndImage(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateTier() {
        if (sumOfTokens > 0) {
            this.tier = checkTier(sumOfTokens);
            return;
        }
        if (blockchains.isEmpty()) {
            this.tier = checkTier(0L);
            return;
        }

        long amount = this.blockchains.stream()
                .map(Blockchain::getSumOfAmount)
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

    public int getCommitSumWithRelation() {
        if (this.commit == null) {
            return 0;
        }
        return this.commit.getAmount();
    }

    public int getIssueSumWithRelation() {
        if (this.issue == null) {
            return 0;
        }
        return this.issue.getAmount();
    }

    public int getPullRequestSumWithRelation() {
        if (this.pullRequest == null) {
            return 0;
        }
        return this.pullRequest.getAmount();
    }

    public int getCodeReviewSumWithRelation() {
        if (this.codeReview == null) {
            return 0;
        }
        return this.codeReview.getAmount();
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

    public Optional<Integer> getSumOfCodeReviews() {
        return Optional.ofNullable(sumOfCodeReviews);
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

    public void deleteContributions() {
        this.commit.delete();
        this.pullRequest.delete();
        this.issue.delete();
        this.codeReview.delete();

        this.commit = null;
        this.pullRequest = null;
        this.issue = null;
        this.codeReview = null;

        this.blockchains.forEach(Blockchain::delete);
        this.blockchains.clear();

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

    public Integer getContributionSum() {
        if (this.commit == null || this.issue == null || this.pullRequest == null || this.codeReview == null) return -1;
        return getCommitSumWithRelation() + getPullRequestSumWithRelation() + getIssueSumWithRelation() + getCodeReviewSumWithRelation();
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
