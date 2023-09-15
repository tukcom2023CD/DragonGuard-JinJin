package com.dragonguard.backend.domain.member.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GithubClientComponent {
    COMMIT_COMPONENT("memberCommitClient"),
    ISSUE_COMPONENT("memberIssueClient"),
    PULL_REQUEST_COMPONENT("memberPullRequestClient"),
    CODE_REVIEW_COMPONENT("memberCodeReviewClient"),
    MEMBER_REPOSITORY_COMPONENT("memberRepoClient"),
    MEMBER_ORGANIZATION_COMPONENT("memberOrganizationClient"),
    ORGANIZATION_REPOSITORY_COMPONENT("organizationRepoClient");

    private final String toComponentName;

    public String getName() {
        return this.toComponentName;
    }
}
