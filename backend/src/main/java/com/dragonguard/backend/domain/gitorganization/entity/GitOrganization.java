package com.dragonguard.backend.domain.gitorganization.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 김승진
 * @description 깃허브 Organization DB Entity
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitOrganization implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "gitOrganization")
    private List<GitOrganizationMember> gitOrganizationMembers = new ArrayList<>();

    private String profileImage;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitOrganization(String name, String profileImage, Member member) {
        this.name = name;
        this.profileImage = profileImage;
        addGitOrganizationMember(member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitOrganization that = (GitOrganization) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void addGitOrganizationMember(Member member) {
        this.gitOrganizationMembers.add(new GitOrganizationMember(this, member));
    }
}
