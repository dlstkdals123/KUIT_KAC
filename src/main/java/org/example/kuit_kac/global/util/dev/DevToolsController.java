package org.example.kuit_kac.global.util.dev;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.terms.repository.UserTermAgreementRepository;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dev-tools")
@RequiredArgsConstructor
@Profile("local") // 로컬 환경에서만 동작
public class DevToolsController {

    private final UserTermAgreementRepository userTermRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    @Operation(
            summary = "테스트용: 특정 유저 데이터 초기화",
            description = "테스트 목적의 엔드포인트. 지정한 userId의 약관 동의 및 온보딩 데이터를 모두 삭제합니다. (실 서비스에서 사용 금지)",
            parameters = {
                    @Parameter(name = "userId", description = "초기화할 사용자 ID", in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "초기화 완료"),
                    @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
            }
    )
    @DeleteMapping("/reset-user/{userId}")
    public ResponseEntity<?> resetUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        // 약관 동의 기록 삭제
        userTermRepository.deleteByIdUserId(userId);

        // 온보딩 정보 삭제
        userInfoRepository.deleteById(userId);

        return ResponseEntity.ok(Map.of("message", "User " + userId + " reset completed"));
    }
}