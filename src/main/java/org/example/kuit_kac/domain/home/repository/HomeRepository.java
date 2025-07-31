package org.example.kuit_kac.domain.home.repository;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<User, Long> {
}
