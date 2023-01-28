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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String githubId;

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Commit> commits = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Enumerated(EnumType.STRING)
    private AuthStep authStep;

    @Builder
    public Member(String name, String githubId, Commit commit) {
        this.name = name;
        this.githubId = githubId;
        this.tier = Tier.UNKNOWN;
        this.authStep = AuthStep.NONE;
        addCommit(commit);
    }

    public void addCommit(Commit commit) {
        if(this.commits.contains(commit)){
            throw new CommitDuplicateException();
        }
        this.commits.add(commit);
    }
}
