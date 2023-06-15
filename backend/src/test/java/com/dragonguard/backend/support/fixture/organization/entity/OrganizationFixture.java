package com.dragonguard.backend.support.fixture.organization.entity;

import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrganizationFixture {
    TUKOREA("한국공학대학교", OrganizationType.UNIVERSITY, "tukorea.ac.kr"),
    GOOGLE("Google", OrganizationType.COMPANY, "gmail.com"),
    SNU("서울대학교", OrganizationType.UNIVERSITY, "snu.ac.kr");

    String name;
    OrganizationType organizationType;
    String emailEndpoint;

    public Organization toEntity() {
        return new Organization(name, organizationType, emailEndpoint);
    }
}
