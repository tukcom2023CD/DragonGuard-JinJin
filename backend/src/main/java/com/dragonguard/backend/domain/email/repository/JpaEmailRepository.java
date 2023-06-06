package com.dragonguard.backend.domain.email.repository;

import com.dragonguard.backend.domain.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailRepository extends JpaRepository<Email, Long>, EmailRepository {}
