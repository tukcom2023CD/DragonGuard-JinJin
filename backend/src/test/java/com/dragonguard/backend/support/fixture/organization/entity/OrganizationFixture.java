package com.dragonguard.backend.support.fixture.organization.entity;

import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrganizationFixture {
    SAMPLE1("한국공학대학교", OrganizationType.UNIVERSITY, "tukorea.ac.kr"),
    SAMPLE2("Google", OrganizationType.COMPANY, "gmail.com"),
    SAMPLE3("서울대학교", OrganizationType.UNIVERSITY, "snu.ac.kr");

    String name;
    OrganizationType organizationType;
    String emailEndpoint;

    public Organization toEntity() {
        return new Organization(name, organizationType, emailEndpoint);
    }
}
