package com.dragonguard.backend.domain.member.service;

import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.codereview.entity.CodeReview;
import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.global.service.ContributionService;
import com.dragonguard.backend.global.service.TransactionService;
import lombok.RequiredArgsConstructor;

@TransactionService
@RequiredArgsConstructor
public class MemberContributionService {
    private final ContributionService<CodeReview, Long> codeReviewService;
    private final ContributionService<PullRequest, Long> pullRequestService;
    private final ContributionService<Issue, Long> issueService;
    private final ContributionService<Commit, Long> commitService;

    public void saveCodeReview(final Member member, final int codeReviewNum, final Integer year) {
        codeReviewService.saveContribution(member, codeReviewNum, year, ContributeType.CODE_REVIEW);
    }

    public void savePullRequest(final Member member, final int pullRequestNum, final Integer year) {
        pullRequestService.saveContribution(member, pullRequestNum, year, ContributeType.PULL_REQUEST);
    }

    public void saveIssue(final Member member, final int issueNum, final Integer year) {
        issueService.saveContribution(member, issueNum, year, ContributeType.ISSUE);
    }

    public void saveCommit(final Member member, final int commitNum, final Integer year) {
        commitService.saveContribution(member, commitNum, year, ContributeType.COMMIT);
    }
}
