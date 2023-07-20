package com.dragonguard.backend.domain.gitrepo.entity;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 김승진
 * @description 깃허브 Repository 정보를 담는 DB Entity
 */

@Getter
@Entity
@EqualsAndHashCode(of = "name")
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitRepo implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "gitRepo", cascade = CascadeType.PERSIST)
    private Set<GitRepoMember> gitRepoMembers = new HashSet<>();

    @JoinColumn
    @ElementCollection
    private List<Integer> sparkLine = new ArrayList<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitRepo(String name) {
        this.name = name;
    }

    public void organizeGitRepoMember(GitRepoMember gitRepoMember) {
        this.gitRepoMembers.add(gitRepoMember);
    }

    public void updateSparkLine(List<Integer> sparkLine) {
        this.sparkLine = sparkLine;
    }
}
