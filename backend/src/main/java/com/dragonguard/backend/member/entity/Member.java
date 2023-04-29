package com.dragonguard.backend.member.entity;

import com.dragonguard.backend.blockchain.entity.Blockchain;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.gitorganization.entity.GitOrganizationMember;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import com.dragonguard.backend.issue.entity.Issue;
import com.dragonguard.backend.member.exception.TierNoneMatchException;
import com.dragonguard.backend.pullrequest.entity.PullRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    @Column(nullable = false, unique = true)
    private String githubId;

    private String profileImage;

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Commit> commits = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Issue> issues = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<PullRequest> pullRequests = new ArrayList<>();

    private String walletAddress;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Enumerated(EnumType.STRING)
    private AuthStep authStep;

    @OneToMany(mappedBy = "member")
    private List<Blockchain> blockchains = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> role = new ArrayList<>(List.of(Role.ROLE_USER));

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gitOrganization")
    private List<GitOrganizationMember> gitOrganizationMembers = new ArrayList<>();

    private String refreshToken;

    private String githubToken;

    @Embedded
    private OrganizationDetails organizationDetails;

    private Integer sumOfReviews;

    @Formula("(SELECT sum(c.amount) FROM commit c WHERE c.member_id = id)")
    private Integer sumOfCommits;

    @Formula("(SELECT sum(i.amount) FROM issue i WHERE i.member_id = id)")
    private Integer sumOfIssues;

    @Formula("(SELECT sum(pr.amount) FROM pull_request pr WHERE pr.member_id = id)")
    private Integer sumOfPullRequests;

    @Formula("(SELECT sum(b.amount) FROM blockchain b WHERE b.member_id = id)")
    private Long sumOfTokens;

    @Builder
    public Member(String name, String githubId, Commit commit, String walletAddress, String profileImage, Role role, AuthStep authStep) {
        this.name = name;
        this.githubId = githubId;
        this.walletAddress = walletAddress;
        this.profileImage = profileImage;
        this.tier = Tier.SPROUT;
        this.authStep = authStep;
        addCommit(commit);
        if (role != null && role.equals(Role.ROLE_ADMIN)) {
            this.role.add(Role.ROLE_ADMIN);
        }
    }

    public void addCommit(Commit commit) {
        if (commit == null || this.commits.stream().anyMatch(commit::equals)) return;
        else if (this.commits.stream().anyMatch(commit::customEquals)) {
            this.commits.stream().filter(commit::customEquals).findFirst().ifPresent(this.commits::remove);
        }
        this.commits.add(commit);
    }

    public void addIssue(Issue issue) {
        if (issue == null || this.issues.stream().anyMatch(issue::equals)) return;
        else if (this.issues.stream().anyMatch(issue::equals)) {
            this.issues.stream().filter(issue::customEquals).findFirst().ifPresent(this.issues::remove);
        }
        this.issues.add(issue);
    }

    public void addPullRequest(PullRequest pullRequest) {
        if (pullRequest == null || this.pullRequests.stream().anyMatch(pullRequest::equals)) return;
        else if (this.pullRequests.stream().anyMatch(pullRequest::customEquals)) {
            this.pullRequests.stream().filter(pullRequest::customEquals).findFirst().ifPresent(this.commits::remove);
        }
        this.pullRequests.add(pullRequest);
    }

    public void updateNameAndImage(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateTier() {
        if (sumOfTokens != null) {
            this.tier = Tier.checkTier(sumOfTokens);
            return;
        }
        Long amount = this.blockchains.stream()
                .map(Blockchain::getAmount)
                .mapToLong(b -> Long.parseLong(b.toString()))
                .sum();
        try {
            this.tier = Tier.checkTier(amount);
        } catch (TierNoneMatchException e) {
        }
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

    public void updateOrganization(Long organizationId, String organizationEmail) {
        this.organizationDetails = new OrganizationDetails(organizationId, organizationEmail);
    }

    public void finishAuth() {
        this.authStep = AuthStep.ALL;
    }

    public void organizeGitOrganizationMember(GitOrganizationMember gitOrganizationMembers) {
        this.gitOrganizationMembers.add(gitOrganizationMembers);
    }

    public void updateSumOfReviews(Integer sumOfReviews) {
        this.sumOfReviews = sumOfReviews;
    }

    public void updateAuthStep(AuthStep authStep) {
        this.authStep = authStep;
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

    public boolean isWallAddressExist() {
        return StringUtils.hasText(this.getWalletAddress());
    }

    public String getBlockchainUrl() {
        if (!StringUtils.hasText(this.walletAddress)) {
            return null;
        }
        return "https://baobab.scope.klaytn.com/account/" + this.walletAddress + "?tabId=txList";
    }
}
