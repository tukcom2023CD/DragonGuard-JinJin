package com.dragonguard.backend.domain.member.dto.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContributionServiceComponent {
    COMMIT_COMPONENT("commitService"),
    ISSUE_COMPONENT("issueService"),
    PULL_REQUEST_COMPONENT("pullRequestService"),
    CODE_REVIEW_COMPONENT("codeReviewService");

    private final String toComponentName;

    public String getName() {
        return this.toComponentName;
    }
}
