package org.example.kuit_kac.domain.user_information.repository;

import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInformation, Long> {
    Optional<UserInformation> findByUserId(Long userId);



    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    boolean existsByUserId(Long existingId);


}
