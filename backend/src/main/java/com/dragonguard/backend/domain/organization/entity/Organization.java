package com.dragonguard.backend.domain.organization.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;

import lombok.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) 정보를 담는 DB Entity
 */
@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization implements Auditable {
    @Where(clause = "auth_step = 'ALL'")
    @OneToMany(mappedBy = "organization")
    private final Set<Member> members = new HashSet<>();

    @Id @GeneratedValue private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    @Column(nullable = false)
    private String emailEndpoint;

    @Enumerated(EnumType.STRING)
    private OrganizationStatus organizationStatus = OrganizationStatus.REQUESTED;

    @Formula(
            "(SELECT COALESCE(sum(h.amount), 0) FROM history h LEFT JOIN blockchain b on h.blockchain_id = b.id LEFT JOIN member m ON m.id = b.member_id "
                    + "WHERE m.organization_id = id and m.auth_step = 'ALL')")
    private Long sumOfMemberTokens;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Organization(
            final String name,
            final OrganizationType organizationType,
            final String emailEndpoint) {
        this.name = name;
        this.organizationType = organizationType;
        validateEmailEndPoint(emailEndpoint);
    }

    private void validateEmailEndPoint(final String emailEndpoint) {
        if (validEmailEndpoint(emailEndpoint)) {
            this.emailEndpoint = emailEndpoint.strip();
        }
    }

    public void addMember(final Member member, final String emailAddress) {
        if (emailAddress.endsWith(emailEndpoint)) {
            this.members.add(member);
            member.updateOrganization(this, emailAddress);
        }
    }

    public void updateStatus(final OrganizationStatus organizationStatus) {
        if (organizationStatus.isAccepted()) {
            updateToAccepted();
            return;
        }
        deleteWhenDenied();
    }

    private void deleteWhenDenied() {
        this.members.forEach(Member::undoFinishingAuthAndDeleteOrganization);
        delete();
    }

    private void updateToAccepted() {
        this.organizationStatus = OrganizationStatus.ACCEPTED;
        this.members.forEach(m -> m.finishAuth(this));
    }

    private boolean validEmailEndpoint(final String emailEndpoint) {
        return StringUtils.hasText(emailEndpoint) && !emailEndpoint.contains("@");
    }

    public void deleteMember(final Member member) {
        this.members.remove(member);
    }
}
