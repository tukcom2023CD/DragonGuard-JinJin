package com.dragonguard.backend.member.entity;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.global.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String githubId;

    private String profileImage;

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Commit> commits = new ArrayList<>();

    private Integer commitsSum;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Enumerated(EnumType.STRING)
    private AuthStep authStep;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String name, String githubId, Commit commit) {
        this.name = name;
        this.githubId = githubId;
        this.tier = Tier.SPROUT;
        this.authStep = AuthStep.NONE;
        this.role = Role.ROLE_USER;
        addCommit(commit);
    }

    public void addCommit(Commit commit) {
        if(this.commits.contains(commit)){
            return;
        }
        this.commits.add(commit);
    }

    public Integer evaluateCommitsSum() {
        this. commitsSum = this.commits.stream().mapToInt(Commit::getCommitNum).sum();
        return commitsSum;
    }
    public void updateNameAndImage(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateTier(Tier tier) {
        this.tier = tier;
    }
}
