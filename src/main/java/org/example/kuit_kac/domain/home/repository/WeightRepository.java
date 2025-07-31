package org.example.kuit_kac.domain.home.repository;

import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.home.model.Weight;
import org.example.kuit_kac.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {
    Optional<Weight> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
