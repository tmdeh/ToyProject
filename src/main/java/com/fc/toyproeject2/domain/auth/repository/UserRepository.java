package com.fc.toyproeject2.domain.auth.repository;

import com.fc.toyproeject2.domain.auth.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}