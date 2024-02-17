package com.dragonguard.backend.batch;

import com.dragonguard.backend.config.kafka.KafkaConsumerConfig;
import com.dragonguard.backend.config.kafka.KafkaProducerConfig;
import com.dragonguard.backend.domain.gitrepo.repository.GitRepoRepository;
import com.dragonguard.backend.domain.gitrepomember.repository.GitRepoMemberRepository;
import com.dragonguard.backend.support.batch.GitRepoBatchTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@Slf4j
@SpringBatchTest
@AutoConfigureCache
@AutoConfigureDataJpa
@ActiveProfiles("test")
@ImportAutoConfiguration
@AutoConfigureTestDatabase
@EmbeddedKafka(topics = {})
@DisplayName("배치 작업중에서")
@AutoConfigureTestEntityManager
@ExtendWith(SpringExtension.class)
@TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
@ComponentScan(
        excludeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = {KafkaConsumerConfig.class, KafkaProducerConfig.class})
        })
@SpringBootTest(
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@RunWith(SpringRunner.class)
public class WebClientJobBatchTest extends GitRepoBatchTest {
    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private GitRepoMemberRepository gitRepoMemberRepository;
    @Autowired private GitRepoRepository gitRepoRepository;
    @Autowired private EntityManager em;

    @Test
    @DisplayName("깃허브 Rest API를 호출하여 GitRepo의 GitRepoMember를 업데이트하는 작업을 수행하는가")
    public void runBatch() throws Exception {
        gitRepoRepository.saveAll(gitRepos);

        log.info("BEFORE SIZE: {}", gitRepoMemberRepository.findAll().size());

        jobLauncherTestUtils.launchJob();

        em.clear();

        log.info("AFTER SIZE: {}", gitRepoMemberRepository.findAll().size());
    }
}
