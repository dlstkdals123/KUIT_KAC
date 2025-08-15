package org.example.kuit_kac.domain.user_information.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.terms.dto.TermAgreementUpsertRequest;
import org.example.kuit_kac.domain.terms.service.UserTermService;
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
    UserTermService userTermsService;

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
    public Long createUserWithOnboarding(OnboardingRequest req) {
        // 0) kakaoId 필수
        final String kakaoId = Objects.requireNonNull(req.getKakaoId(), "kakaoId required");

        // 1) 닉네임 결정
        final String nickname = (req.getNickname() == null || req.getNickname().isBlank())
                ? "user_" + UUID.randomUUID().toString().substring(0, 8)
                : req.getNickname();

        // 2) 이미 존재하면 “재온보딩” 판단 (아이템포턴트 정책)
        Optional<User> existing = userRepository.findByKakaoId(kakaoId);
        if (existing.isPresent() && userInfoRepository.existsById(existing.get().getId())) {
            // 이미 온보딩 끝난 유저면 409
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 3) User 생성 또는 업데이트
        User user = existing.orElseGet(() ->
                new User(kakaoId, nickname, req.getGender(), req.getAge(), req.getHeight(), req.getTargetWeight())
        );

        if (existing.isPresent()) {
            // 온보딩에서 넘어온 값으로 업데이트(정책: null은 덮지 않음)
            if (req.getGender() != null) user.setGender(req.getGender());
            if (req.getAge() != null) user.setAge(req.getAge());
            if (req.getHeight() != null) user.setHeight(req.getHeight());
            if (req.getTargetWeight() != null) user.setTargetWeight(req.getTargetWeight());
            if (req.getNickname() != null && !req.getNickname().isBlank()) user.setNickname(req.getNickname());
        }

        userRepository.save(user);

        // 4) UserInformation 생성
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

        // 5) 약관 업서트 (있으면)
        if (req.getAgreements() != null && !req.getAgreements().isEmpty()) {
            // 요청 리스트를 그대로 upsert
            var upsertReq = new TermAgreementUpsertRequest(req.getAgreements());
            userTermsService.upsertAgreements(user.getId(), upsertReq);
        }

        // 6) 필수 약관 검증 (정책상 필수라면)
        if (!userTermsService.hasAgreedRequired(user.getId())) {
            // 트랜잭션 전체 롤백 → 클라이언트는 필수 약관 체크하고 재요청
            throw new CustomException(ErrorCode.REQUIRED_TERMS_NOT_AGREED);
        }

        return user.getId();
    }
}

