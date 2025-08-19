package org.example.kuit_kac.domain.food.repository;


import org.example.kuit_kac.domain.food.model.Aifood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AifoodRepository extends JpaRepository<Aifood, Long> {
    List<Aifood> findByUserId(Long userId);
}
