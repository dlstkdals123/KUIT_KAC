package org.example.kuit_kac.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.dto.DietNameResponse;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.user.dto.UserDietResponse;
import org.example.kuit_kac.domain.user.dto.UserResponse;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DietRepository dietRepository;

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public List<UserDietResponse> getUserDietsById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Diet> diets = dietRepository.findByUserId(user.getId());

        List<DietNameResponse> dietNameResponses = diets.stream()
                .map(diet -> new DietNameResponse(diet.getId(), diet.getName()))
                .toList();

        return List.of(new UserDietResponse(user.getId(), dietNameResponses));
    }
}
