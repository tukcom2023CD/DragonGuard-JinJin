package com.dragonguard.backend.config.batch;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GitRepoReader implements ItemReader<GitRepo> {
    private final GitRepoRepository gitRepoRepository;
    private List<GitRepo> list = new ArrayList<>();
    private int nextIndex = 0;

    @PostConstruct
    public void init() {
        list.addAll(gitRepoRepository.findAll());
    }

    @Override
    public synchronized GitRepo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (nextIndex < list.size()) {
            return list.get(nextIndex++);
        }
        return null;
    }
}
