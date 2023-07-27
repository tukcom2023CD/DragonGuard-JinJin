package com.dragonguard.backend.batch;

import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GitRepoMemberWriter implements ItemWriter<List<GitRepoMember>> {
    private final GitRepoMemberRepository gitRepoMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void write(final List<? extends List<GitRepoMember>> items) throws Exception {
        items.stream()
                .flatMap(List::stream)
                .forEach(i -> memberRepository.save(i.getMember()));
    }
}
