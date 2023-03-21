package com.dragonguard.backend.organization.entity;

import com.dragonguard.backend.global.BaseTime;
import com.dragonguard.backend.global.SoftDelete;
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

    private String email;

    @OneToMany
    private Set<Member> members = new HashSet<>();

    @Builder
    public Organization(String name, OrganizationType organizationType, String email) {
        this.name = name;
        this.organizationType = organizationType;
        this.email = email;
    }

    public void addMember(Member member) {
        this.members.add(member);
        member.updateOrganization(id);
    }
}
