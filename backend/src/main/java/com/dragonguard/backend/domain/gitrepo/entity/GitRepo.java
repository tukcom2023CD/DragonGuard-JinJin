package com.dragonguard.backend.domain.gitrepo.entity;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 담는 DB Entity
 */
@Getter
@Entity
@SoftDelete
@EqualsAndHashCode(of = "name")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepo implements Auditable {

    @OneToMany(mappedBy = "gitRepo", cascade = CascadeType.PERSIST)
    private final Set<GitRepoMember> gitRepoMembers = new HashSet<>();

    @Id @GeneratedValue private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @JoinColumn @ElementCollection private List<Integer> sparkLine = new ArrayList<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitRepo(final String name) {
        this.name = name;
    }

    public void organizeGitRepoMember(final GitRepoMember gitRepoMember) {
        this.gitRepoMembers.add(gitRepoMember);
    }

    public void updateSparkLine(final List<Integer> sparkLine) {
        this.sparkLine = sparkLine;
    }
}
