package org.example.kuit_kac.domain.user.repository;

import org.example.kuit_kac.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Stack;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(String kakaoId);

    boolean existsByKakaoId(String kakaoId);

    @Query("select u.id from User u where u.kakaoId = :kakaoId")
    Optional<Long> findIdByKakaoId(@Param("kakaoId") String kakaoId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM `user` WHERE id = :userId", nativeQuery = true)
    int deleteCascadeById(@Param("userId") Long userId);
}
