package org.example.kuit_kac.domain.user_information.repository;

import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    Optional<UserInformation> findByUserId(long userId);
}
