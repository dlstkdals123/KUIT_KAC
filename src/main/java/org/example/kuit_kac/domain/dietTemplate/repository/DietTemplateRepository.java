package org.example.kuit_kac.domain.dietTemplate.repository;

import org.example.kuit_kac.domain.dietTemplate.model.DietTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietTemplateRepository extends JpaRepository<DietTemplate, Long> {

}
