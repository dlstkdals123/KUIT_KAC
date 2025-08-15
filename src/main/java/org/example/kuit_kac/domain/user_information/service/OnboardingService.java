package org.example.kuit_kac.domain.user_information.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.terms.dto.TermAgreementUpsertRequest;
import org.example.kuit_kac.domain.terms.service.UserTermsService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.domain.user_information.dto.OnboardingRequest;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final UserTermsService userTermsService;

    @Transactional(readOnly = true)
    public UserInformation getUserInformationByUserId(long userId) {
        return userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
    }

    @Transactional(readOnly = true)
    public boolean isOnboardingRequired(long userId) {
        return userInfoRepository.findByUserId(userId)
                .map(info -> {
                    // 온보딩 완료 여부 판단
                    if (!StringUtils.hasText(info.getDietFailReason())) return true;
                    if (info.getAppetiteType() == null) return true;
                    if (!StringUtils.hasText(info.getWeeklyEatingOutCount())) return true;
                    if (info.getEatingOutType() == null) return true;
                    if (info.getDietVelocity() == null) return true;

                    return false; //  전부 값이 있으면 온보딩 불필요
                })
                .orElse(true); // 온보딩 필요
    }

    @Transactional
    public Long createUserWithOnboarding(String kakaoId, OnboardingRequest req) {

        // 0) kakaoId 필수
        Objects.requireNonNull(kakaoId, "kakaoId required");

        // 1) 닉네임 결정
        final String nickname = (req.getNickname() == null || req.getNickname().isBlank())
                ? "user_" + UUID.randomUUID().toString().substring(0, 8)
                : req.getNickname();

        // 2) 기존 유저 조회 (아이템포턴트)
        Optional<User> existingOpt = userRepository.findByKakaoId(kakaoId);
        if (existingOpt.isPresent()) {
            Long existingId = existingOpt.get().getId();
            // 이미 온보딩 끝난 유저면 차단
            if (userInfoRepository.existsById(existingId)) {
                throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
            }
        }

        // 3) User 생성 또는 업데이트
        User user = existingOpt.orElseGet(() ->
                new User(kakaoId, nickname, req.getGender(), req.getAge(), req.getHeight(), req.getTargetWeight())
        );
        // 4) UserInformation 생성/업서트 (@MapsId 가정: PK = user_id)
        UserInformation info = new UserInformation(
                user,
                req.isHasDietExperience(),
                req.getDietFailReason(),
                req.getAppetiteType(),
                req.getWeeklyEatingOutCount(),
                req.getEatingOutType(),
                req.getDietVelocity()
        );
        userInfoRepository.save(info);

        // 5) 약관 업서트(요청에 있으면)
        if (req.getAgreements() != null && !req.getAgreements().isEmpty()) {
            userTermsService.upsertAgreements(user.getId(),
                    new TermAgreementUpsertRequest(req.getAgreements()));
        }

        // 6) 필수 약관 검증(정책상 필수일 경우)
        if (!userTermsService.hasAgreedRequired(user.getId())) {
            throw new CustomException(ErrorCode.REQUIRED_TERMS_NOT_AGREED);
        }

        return user.getId();
    }
}


