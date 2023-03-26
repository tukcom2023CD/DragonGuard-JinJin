package com.dragonguard.backend.organization.entity;

import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import com.dragonguard.backend.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    private String emailEndpoint;

    @OneToMany
    private Set<Member> members = new HashSet<>();

    @Builder
    public Organization(String name, OrganizationType organizationType, String emailEndpoint) {
        this.name = name;
        this.organizationType = organizationType;
        this.emailEndpoint = emailEndpoint;
    }

    public void addMember(Member member, String email) {
        if (email.endsWith(emailEndpoint)) {
            this.members.add(member);
            member.updateOrganization(id, email);
        }
    }
}
