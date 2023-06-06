package com.dragonguard.backend.domain.organization.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Formula;
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
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    private String emailEndpoint;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "organization")
    private Set<Member> members = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private OrganizationStatus organizationStatus = OrganizationStatus.REQUESTED;

    @Formula("(SELECT sum(b.amount) FROM blockchain b INNER JOIN member m ON m.id = b.member_id WHERE m.organization_id = id)")
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
            this.emailEndpoint = emailEndpoint;
        }
    }

    public void addMember(Member member, String email) {
        if (email.endsWith(emailEndpoint)) {
            this.members.add(member);
            member.updateOrganization(this, email);
        }
    }

    public void updateStatus(OrganizationStatus organizationStatus) {
        this.organizationStatus = organizationStatus;
    }

    private boolean validateEmailEndpoint(String emailEndpoint) {
        return StringUtils.hasText(emailEndpoint) && !emailEndpoint.contains("@");
    }
}
