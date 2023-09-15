package com.dragonguard.backend.domain.member.entity;

import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.gitorganization.entity.GitOrganizationMember;
import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.member.exception.InvalidGithubTokenException;
import com.dragonguard.backend.domain.member.exception.NoSuchWalletAddressException;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 멤버 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@EqualsAndHashCode(of = "githubId")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(name = "member_index", columnList = "githubId")})
public class Member implements Auditable {
    private static final String BLOCKCHAIN_URL = "https://baobab.scope.klaytn.com/account/%s?tabId=txList";
    private static final Long NO_TOKEN = 0L;

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

    @OneToMany(mappedBy = "member", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<GitRepoMember> gitRepoMembers = new ArrayList<>();

    @JoinColumn
    @Where(clause = "organization_status = 'ACCEPTED'")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @JoinColumn
    @Enumerated(EnumType.STRING)
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
    public Member(final String name, final String githubId, final String walletAddress, final String profileImage, final Role role, final AuthStep authStep) {
        this.name = name;
        this.githubId = githubId;
        this.walletAddress = walletAddress;
        this.profileImage = profileImage;
        this.tier = Tier.SPROUT;
        this.authStep = authStep;
        addRoleIfAdmin(role);
    }

    private void addRoleIfAdmin(final Role role) {
        if (role != null && role.equals(Role.ROLE_ADMIN)) {
            this.role.add(role);
        }
    }

    public void addCommit(final Commit commit) {
        this.commit = commit;
    }

    public void addIssue(final Issue issue) {
        this.issue = issue;
    }

    public void addPullRequest(final PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }

    public void addCodeReview(final CodeReview codeReview) {
        this.codeReview = codeReview;
    }

    public void updateTier() {
        if (sumOfTokens != null && sumOfTokens > NO_TOKEN) {
            this.tier = checkTier(sumOfTokens);
            return;
        }
        this.tier = checkTier(getSumOfTokensWithBlockchain());
    }

    private long getSumOfTokensWithBlockchain() {
        if (blockchains.isEmpty()) {
            return NO_TOKEN;
        }

        return this.blockchains.stream()
                .map(Blockchain::getSumOfAmount)
                .mapToLong(b -> Long.parseLong(b.toString()))
                .sum();
    }

    public Tier checkTier(long amount) {
        return Tier.checkTier(amount);
    }

    public void updateWalletAddress(final String walletAddress) {
        if (!authStep.isAll()) {
            this.authStep = AuthStep.GITHUB_AND_KLIP;
            this.walletAddress = walletAddress;
        }
    }

    public List<SimpleGrantedAuthority> getRole() {
        return role.stream().map(Role::name).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public void updateGithubToken(final String githubToken) {
        this.githubToken = githubToken;
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateOrganization(final Organization organization, final String emailAddress) {
        this.organization = organization;
        this.emailAddress = emailAddress;
    }

    public void organizeGitOrganizationMember(final GitOrganizationMember gitOrganizationMember) {
        this.gitOrganizationMembers.add(gitOrganizationMember);
    }

    public void organizeGitRepoMember(final GitRepoMember gitRepoMember) {
        this.gitRepoMembers.add(gitRepoMember);
    }

    public void finishAuth(final Organization organization) {
        this.authStep = AuthStep.ALL;
        this.organization = organization;
    }

    public boolean isWalletAddressExists() {
        return StringUtils.hasText(this.getWalletAddress());
    }

    public String getBlockchainUrl() {
        if (!isWalletAddressExists()) {
            throw new NoSuchWalletAddressException();
        }
        return String.format(BLOCKCHAIN_URL, this.walletAddress);
    }

    public String getGithubToken() {
        validateGithubToken();
        return this.githubToken;
    }

    private void validateGithubToken() {
        if (!StringUtils.hasText(this.githubToken)) {
            throw new InvalidGithubTokenException();
        }
    }

    public List<String> getGitRepoNames() {
        return this.gitRepoMembers.stream().map(GitRepoMember::getGitRepo).map(GitRepo::getName).collect(Collectors.toList());
    }

    public String getOrganizationName() {
        if (this.organization == null) {
            return null;
        }
        return this.organization.getName();
    }

    public void validateWalletAddressAndUpdateTier() {
        if (isWalletAddressExists()) {
            updateTier();
        }
    }

    public void organizeBlockchain(final Blockchain blockchain) {
        this.blockchains.add(blockchain);
        updateTier();
    }

    public boolean isServiceMember() {
        return !this.authStep.isServiceMemberAuthStep();
    }

    public void updateAuthStepAndNameAndProfileImage(final AuthStep authStep, final String name, final String profileImage) {
        this.authStep = authStep;
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateProfileImage(final String profileImage) {
        this.profileImage = profileImage;
    }

    public void undoFinishingAuthAndDeleteOrganization() {
        this.organization = null;
        this.authStep = AuthStep.GITHUB_AND_KLIP;
    }

    public void withdraw() {
        this.walletAddress = null;
        this.tier = Tier.SPROUT;
        this.authStep = AuthStep.NONE;
        this.commit.delete();
        this.commit = null;
        this.issue.delete();
        this.issue = null;
        this.pullRequest.delete();
        this.pullRequest = null;
        this.codeReview.delete();
        this.codeReview = null;
        this.blockchains.forEach(Blockchain::deleteByMember);
        this.blockchains = new ArrayList<>();
        this.organization.deleteMember(this);
        this.organization = null;
        this.role = new ArrayList<>();
        this.refreshToken = null;
        this.githubToken = null;
        this.emailAddress = null;
    }
}
