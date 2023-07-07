package com.dragonguard.backend.domain.gitorganization.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 깃허브 Organization 과 Member 사이의 M 대 N 관계 중간 엔티티
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitOrganizationMember implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private GitOrganization gitOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "BINARY(16)")
    private Member member;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public GitOrganizationMember(GitOrganization gitOrganization, Member member) {
        this.gitOrganization = gitOrganization;
        this.member = member;
        organize();
    }

    private void organize() {
        this.member.organizeGitOrganizationMember(this);
    }
}
