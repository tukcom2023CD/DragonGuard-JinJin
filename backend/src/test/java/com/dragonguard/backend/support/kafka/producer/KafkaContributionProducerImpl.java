package com.dragonguard.backend.support.kafka.producer;

import com.dragonguard.backend.domain.commit.entity.Commit;
import com.dragonguard.backend.domain.commit.repository.CommitRepository;
import com.dragonguard.backend.domain.contribution.dto.request.ContributionScrapingRequest;
import com.dragonguard.backend.domain.issue.entity.Issue;
import com.dragonguard.backend.domain.issue.repository.IssueRepository;
import com.dragonguard.backend.domain.member.repository.MemberQueryRepository;
import com.dragonguard.backend.domain.pullrequest.entity.PullRequest;
import com.dragonguard.backend.domain.pullrequest.repository.PullRequestRepository;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Primary
@Component
@RequiredArgsConstructor
public class KafkaContributionProducerImpl implements KafkaProducer<ContributionScrapingRequest> {
    private final MemberQueryRepository memberQueryRepository;
    private final CommitRepository commitRepository;
    private final PullRequestRepository pullRequestRepository;
    private final IssueRepository issueRepository;

    @Override
    public void send(ContributionScrapingRequest request) {
        memberQueryRepository.findByGithubId(request.getGithubId()).ifPresent(member -> {
            int year = LocalDate.now().getYear();
            member.updateWalletAddress(null);

            commitRepository.save(Commit.builder().amount(100).year(year).member(member).build());
            pullRequestRepository.save(PullRequest.builder().amount(200).year(year).member(member).build());
            issueRepository.save(Issue.builder().amount(300).year(year).member(member).build());

            commitRepository.findAllByMember(member).forEach(member::addCommit);
            pullRequestRepository.findAllByMember(member).forEach(member::addPullRequest);
            issueRepository.findAllByMember(member).forEach(member::addIssue);
            member.updateSumOfReviews(400);
        });
    }
}
