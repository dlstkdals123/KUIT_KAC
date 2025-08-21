package org.example.kuit_kac.domain.user_information.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.config.AuthOnboardingProperties;
import org.example.kuit_kac.domain.home.service.WeightService;
import org.example.kuit_kac.domain.terms.dto.TermAgreementUpsertRequest;
import org.example.kuit_kac.domain.terms.repository.UserTermAgreementRepository;
import org.example.kuit_kac.domain.terms.service.UserTermsService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.domain.user_information.dto.OnboardingRequest;
import org.example.kuit_kac.domain.user_information.dto.OnboardingResponse;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.global.util.dev.DevAutofillProperties;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final WeightService weightService;

    // 선택 주입(없으면 기본값 사용)
    private final ObjectProvider<DevAutofillProperties> autofillProvider;
    private final ObjectProvider<AuthOnboardingProperties> onboardingPropsProvider;

    // 조회
    @Transactional(readOnly = true)
    public UserInformation getUserInformationByUserId(long userId) {
        return userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private boolean isOnboardingIncomplete(UserInformation info) {
        if (!StringUtils.hasText(info.getDietFailReason())) return true;
        if (info.getAppetiteType() == null) return true;
        if (!StringUtils.hasText(info.getWeeklyEatingOutCount())) return true;
        if (info.getEatingOutType() == null) return true;
        if (info.getDietVelocity() == null) return true;
        if (info.getActivity() == null) return true;
        return false;
    }

    // 생성

    /**
     * 기본 온보딩 생성 (프로퍼티 기반 설정 사용)
     * - debug.autofill.enabled가 true이면 누락 필드 자동 채움
     * - auth.onboarding.require가 true이면 필수 약관 강제
     */
//    @Transactional
//    public OnboardingResponse createUserWithOnboarding(String kakaoId, OnboardingRequest req) {
//        boolean allowAutofill = autofillProvider.getIfAvailable(() -> null) != null
//                && autofillProvider.getObject().isEnabled();
//
//        boolean enforceTerms = onboardingPropsProvider.getIfAvailable(() -> null) == null
//                || onboardingPropsProvider.getObject().isRequire();
//
//        return createUserWithOnboarding(kakaoId, req, allowAutofill, enforceTerms);
//    }

    /**
     * 세밀 제어 버전 (컨트롤러에서 DEV 여부로 넘겨줌)
     */
    @Transactional
    public OnboardingResponse createUserWithOnboarding(String kakaoId,
                                                       OnboardingRequest req,
                                                       boolean allowAutofill) {
        boolean enforceTerms = false;
        return createUserWithOnboarding(kakaoId, req, allowAutofill, enforceTerms);
    }

    /**
     * 가장 하위 구현
     */
    @Transactional
    public OnboardingResponse createUserWithOnboarding(String kakaoId,
                                                       OnboardingRequest req,
                                                       boolean allowAutofill,
                                                       boolean enforceRequiredTerms) {

        // 0) kakaoId 필수
        Objects.requireNonNull(kakaoId, "kakaoId required");

        // 1) 기존 유저 존재 여부
        Optional<User> existingOpt = userRepository.findByKakaoId(kakaoId);
        if (existingOpt.isPresent()) {
            Long existingId = existingOpt.get().getId();
            if (userInfoRepository.existsByUserId(existingId)) { // ✅ userId 기준 체크
                throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
            }
        }

        // 2) 필수 user 필드 준비 (닉네임/성별/나이/키/목표체중)
        PreparedUserFields fields = prepareUserFields(kakaoId, req, allowAutofill);

        // 3) User 생성/저장 (cascade가 보장되지 않는 매핑 대비)
        User user = existingOpt.orElseGet(() ->
                new User(kakaoId, fields.nickname, fields.gender, fields.age, fields.height, fields.targetWeight)
        );


        if (existingOpt.isEmpty()) {
            // 신규면 저장(닉네임 unique 제약 방어 포함)
            user = persistUserWithUniqueNickname(user, fields.nickname);
        } else {
            // 기존인데 아직 온보딩 전이면 필요한 필드 업데이트
            existingOpt.get().setNickname(fields.nickname);
            existingOpt.get().setGender(fields.gender);
            existingOpt.get().setAge(fields.age);
            existingOpt.get().setHeight(fields.height);
            existingOpt.get().setTargetWeight(fields.targetWeight);
        }

        // 현재 몸무게 저장
        if (fields.currentWeight != null) {
            weightService.saveOrUpdateTodayWeight(user.getId(), fields.currentWeight);
        }

        // 4) UserInformation 생성/저장 (PK=@MapsId(user_id) 가정)
        UserInformation info = new UserInformation(
                user,
                req.isHasDietExperience(),
                req.getDietFailReason(),
                req.getAppetiteType(),
                req.getWeeklyEatingOutCount(),
                req.getEatingOutType(),
                req.getDietVelocity(),
                req.getActivity()
        );
        userInfoRepository.save(info);

//        // 5) 약관 업서트 (요청에 있으면)
//        if (req.getAgreements() != null && !req.getAgreements().isEmpty()) {
//            userTermsService.upsertAgreements(user.getId(),
//                    new TermAgreementUpsertRequest(req.getAgreements()));
//        }


        // 6) 필수 약관 검증(정책상 강제일 경우)
//        if (enforceRequiredTerms && !userTermsService.hasAgreedRequired(user.getId())) {
//            throw new CustomException(ErrorCode.REQUIRED_TERMS_NOT_AGREED);
//        }

        //기초대사량 계산
        long userId = user.getId();
        UserInformation userInfo = this.getUserInformationByUserId(userId);
        double weightValue = weightService.getLatestWeightByUserId(userId).getWeight();

        double bmr = user.getBMR(weightValue);

        // 목표까지 감량해야 할 몸무게
        double TargetWeightLoss = Math.max(weightValue - user.getTargetWeight(), 0);
        int dietDays = userInfo.getDietVelocity().getPeriodInDays();
        double dailyDeficit = (TargetWeightLoss * 7700) / dietDays; // 감량칼로리 / 다이어트기간

        return new OnboardingResponse(user.getId(), (int) bmr, (int) dailyDeficit);
    }

    private static class PreparedUserFields {

        final String nickname;
        final org.example.kuit_kac.domain.user.model.GenderType gender;
        final Integer age;
        final Integer height;
        final Double targetWeight;
        final Double currentWeight;

        PreparedUserFields(String nickname,
                           org.example.kuit_kac.domain.user.model.GenderType gender,
                           Integer age, Integer height, Double targetWeight, Double currentWeight) {
            this.nickname = nickname;
            this.gender = gender;
            this.age = age;
            this.height = height;
            this.targetWeight = targetWeight;
            this.currentWeight = currentWeight;
        }

    }

    private PreparedUserFields prepareUserFields(String kakaoId,
                                                 OnboardingRequest req,
                                                 boolean allowAutofill) {

        // 닉네임: 공백이면 자동 생성
        String nickname = (req.getNickname() == null || req.getNickname().isBlank())
                ? genNickname("user", kakaoId)
                : trimToLength(req.getNickname().trim(), 20);

        var gender = req.getGender();
        Integer age = req.getAge();
        Integer height = req.getHeight();
        Double targetWeight = req.getTargetWeight();
        Double currentWeight = req.getCurrentWeight();

        if (allowAutofill) {
            DevAutofillProperties af = autofillProvider.getIfAvailable(() -> null);
            if (af != null) {
                if (gender == null) gender = af.getDefaultGender();
                if (age == null) age = af.getDefaultAge();
                if (height == null) height = af.getDefaultHeight();
                if (targetWeight == null) targetWeight = af.getDefaultTargetWeight();
                if (currentWeight == null) currentWeight = af.getDefaultCurrentWeight();
            }
        }

        // 프로덕션에선 누락되면 아래 단계(DB not-null)에 걸리거나, 별도 Validation으로 막자
        return new PreparedUserFields(nickname, gender, age, height, targetWeight, currentWeight);
    }

    private User persistUserWithUniqueNickname(User user, String baseNickname) {
        String nick = baseNickname;
        int attempt = 0;
        while (true) {
            try {
                // 닉네임 유니크 미리 확인
                if (userRepository.existsByNickname(nick)) {
                    nick = uniquify(nick);
                    continue;
                }
                user.setNickname(nick);
                return userRepository.saveAndFlush(user);
            } catch (DataIntegrityViolationException e) {
                // 레이스 컨디션 대비: unique 제약 위반 시 재시도
                if (++attempt > 10) throw e;
                nick = uniquify(nick);
            }
        }
    }

    private String uniquify(String base) {
        String suffix = "-" + UUID.randomUUID().toString().substring(0, 4);
        int max = 20 - suffix.length();
        String head = trimToLength(base, Math.max(1, max));
        return head + suffix;
    }

    private String genNickname(String prefix, String kakaoId) {
        String kid4 = (kakaoId != null && kakaoId.length() >= 4)
                ? kakaoId.substring(kakaoId.length() - 4) : "dev";
        String nick = prefix + "_" + kid4 + "_" + UUID.randomUUID().toString().substring(0, 4);
        return trimToLength(nick, 20);
    }

    private String trimToLength(String s, int maxLen) {
        if (s == null) return null;
        return (s.length() <= maxLen) ? s : s.substring(0, maxLen);
    }
}
