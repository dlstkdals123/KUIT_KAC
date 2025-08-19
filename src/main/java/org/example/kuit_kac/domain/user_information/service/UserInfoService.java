package org.example.kuit_kac.domain.user_information.service;

import org.example.kuit_kac.domain.user_information.model.UserInformation;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    public UserInformation getUserInformationByUserId(long userId) {
        return userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
