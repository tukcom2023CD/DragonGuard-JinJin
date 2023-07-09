package com.dragonguard.backend.domain.organization.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) 정보를 담는 DB Entity
 */

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    @Column(nullable = false)
    private String emailEndpoint;

    @Where(clause = "auth_step = 'ALL'")
    @OneToMany(mappedBy = "organization")
    private Set<Member> members = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private OrganizationStatus organizationStatus = OrganizationStatus.REQUESTED;

    @Formula("(SELECT COALESCE(sum(h.amount), 0) FROM history h LEFT JOIN blockchain b on h.blockchain_id = b.id LEFT JOIN member m ON m.id = b.member_id " +
            "WHERE m.organization_id = id and m.auth_step = 'ALL')")
    private Long sumOfMemberTokens;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Organization(String name, OrganizationType organizationType, String emailEndpoint) {
        this.name = name;
        this.organizationType = organizationType;
        if (validateEmailEndpoint(emailEndpoint)) {
            this.emailEndpoint = emailEndpoint.strip();
        }
    }

    public void addMember(Member member, String emailAddress) {
        if (emailAddress.endsWith(emailEndpoint)) {
            this.members.add(member);
            member.updateOrganization(this, emailAddress);
        }
    }

    public void updateStatus(OrganizationStatus organizationStatus) {
        if (organizationStatus.equals(OrganizationStatus.ACCEPTED)) {
            this.organizationStatus = OrganizationStatus.ACCEPTED;
            this.members.forEach(m -> m.finishAuth(this));
            return;
        }
        this.members.forEach(Member::undoFinishingAuthAndDeleteOrganization);
        delete();
    }

    private boolean validateEmailEndpoint(String emailEndpoint) {
        return StringUtils.hasText(emailEndpoint) && !emailEndpoint.contains("@");
    }
}
