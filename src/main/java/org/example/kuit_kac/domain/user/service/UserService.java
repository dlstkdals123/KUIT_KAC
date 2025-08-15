package org.example.kuit_kac.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }

    @Transactional(readOnly = true)
    public Optional<Long> findIdByKakaoId(String kakaoId) {
        return userRepository.findIdByKakaoId(kakaoId);
    }

    @Transactional(readOnly = true)
    public boolean existsByKakaoId(String kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }

}
