package org.example.kuit_kac.domain.user_information.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

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
                    // TODO hasDierExperience는 null아닌 false일 경우에 대해 기본값 검토 필요
                    if (!StringUtils.hasText(info.getDietFailReason())) return true;
                    if (info.getAppetiteType() == null) return true;
                    if (!StringUtils.hasText(info.getWeeklyEatingOutCount())) return true;
                    if (info.getEatingOutType() == null) return true;
                    if (info.getDietVelocity() == null) return true;

                    return false; //  전부 값이 있으면 온보딩 불필요
                })
                .orElse(true); // 온보딩 필요
    }
}
