package org.example.kuit_kac.domain.user_information.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.example.kuit_kac.domain.user_information.repository.UserInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInformationService {
    private final UserInformationRepository userInformationRepository;

    @Transactional(readOnly = true)
    public UserInformation getUserInformationByUserId(long userId) {
        return userInformationRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
    }

}
