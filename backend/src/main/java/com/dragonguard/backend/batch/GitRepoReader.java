package com.dragonguard.backend.batch;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GitRepoReader implements ItemReader<GitRepo> {
    private final GitRepoRepository gitRepoRepository;
    private final List<GitRepo> list = new ArrayList<>();
    private int nextIndex = 0;

    @PostConstruct
    public void init() {
        list.addAll(gitRepoRepository.findAll());
    }

    @Override
    public synchronized GitRepo read() {
        if (nextIndex < list.size()) {
            return list.get(nextIndex++);
        }
        return null;
    }
}
