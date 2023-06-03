package com.dragonguard.backend.domain.gitorganization.entity;

import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitOrganization extends BaseTime implements Persistable<String> {

    @Id
    private String id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gitOrganization")
    private List<GitOrganizationMember> gitOrganizationMembers = new ArrayList<>();

    @Transient
    private Boolean update;

    @Builder
    public GitOrganization(String id, Member member, Boolean update) {
        this.id = id;
        this.update = update;
        addGitOrganizationMember(member);
    }

    public void addGitOrganizationMember(Member member) {
        this.gitOrganizationMembers.add(new GitOrganizationMember(this, member));
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return !this.update;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitOrganization that = (GitOrganization) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
