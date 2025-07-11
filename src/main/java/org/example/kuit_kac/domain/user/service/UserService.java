package org.example.kuit_kac.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.dto.UserResponse;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }
}
