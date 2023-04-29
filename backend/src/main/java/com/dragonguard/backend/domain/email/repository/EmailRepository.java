package com.dragonguard.backend.domain.email.repository;

import com.dragonguard.backend.domain.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}
