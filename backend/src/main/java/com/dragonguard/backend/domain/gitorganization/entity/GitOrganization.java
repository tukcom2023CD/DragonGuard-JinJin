package com.dragonguard.backend.domain.gitorganization.entity;

import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 김승진
 * @description 깃허브 Organization DB Entity
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitOrganization extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gitOrganization")
    private List<GitOrganizationMember> gitOrganizationMembers = new ArrayList<>();

    @Builder
    public GitOrganization(String name, Member member) {
        this.name = name;
        addGitOrganizationMember(member);
    }

    public void addGitOrganizationMember(Member member) {
        this.gitOrganizationMembers.add(new GitOrganizationMember(this, member));
    }
}
