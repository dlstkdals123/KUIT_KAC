package org.example.kuit_kac.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.example.kuit_kac.domain.terms.repository.UserTermAgreementRepository;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserTermAgreementRepository userTermRepository;

    @Transactional(readOnly = true)
    // TODO: 토큰으로 본인 맞는지 로직 필요
    // TODO: request에서 이 메서드 부를 때 유저 없을때 뿐만 아니라 본인맞는지도 체크하도록
    // TODO: (안드에서 마스터키로 접근) 마스터키면 무조건 마스터계정 유저 리턴하도록
    // TODO: 마스터계정은 DB에 삽입
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

    @Transactional
    public boolean deleteUserById(long userId) {
        if (!userRepository.existsById(userId)) return false;
        userRepository.deleteById(userId); // FK CASCADE
        return true;
    }

}
