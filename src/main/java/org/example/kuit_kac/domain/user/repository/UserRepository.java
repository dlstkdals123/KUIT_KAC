package org.example.kuit_kac.domain.user.repository;

import org.example.kuit_kac.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
