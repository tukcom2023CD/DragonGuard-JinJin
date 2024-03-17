package com.dragonguard.backend.domain.gitorganization.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 깃허브 Organization DB Entity
 */
@Getter
@Entity
@SoftDelete
@EqualsAndHashCode(of = "name")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitOrganization implements Auditable {

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "gitOrganization")
    private final List<GitOrganizationMember> gitOrganizationMembers = new ArrayList<>();

    @Id @GeneratedValue private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String profileImage;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitOrganization(final String name, final String profileImage, final Member member) {
        this.name = name;
        this.profileImage = profileImage;
        addGitOrganizationMember(member);
    }

    public void addGitOrganizationMember(final Member member) {
        this.gitOrganizationMembers.add(new GitOrganizationMember(this, member));
    }
}
