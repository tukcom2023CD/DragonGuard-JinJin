package com.dragonguard.backend.domain.issue.repository;

import com.dragonguard.backend.domain.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author 김승진
 * @description issue Entity의 DB CRUD를 담당하는 클래스
 */

public interface JpaIssueRepository extends JpaRepository<Issue, Long>, IssueRepository {}
