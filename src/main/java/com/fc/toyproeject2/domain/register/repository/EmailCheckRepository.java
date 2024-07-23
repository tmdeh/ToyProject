package com.fc.toyproeject2.domain.register.repository;

import com.fc.toyproeject2.domain.register.model.entity.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCheckRepository extends JpaRepository<EmailCheck, Long> {

    Optional<EmailCheck> findByEmail(String email);

    void deleteByEmail(String email);

}