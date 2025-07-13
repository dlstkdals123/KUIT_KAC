package org.example.kuit_kac.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.dto.UserDietResponse;
import org.example.kuit_kac.domain.user.dto.UserResponse;
import org.example.kuit_kac.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "사용자 관리", description = "사용자 정보 조회, 생성, 수정, 삭제 등 사용자 관련 모든 기능을 제공합니다.")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "사용자 ID로 단일 사용자 정보 조회", description = "제공된 사용자 ID를 사용하여 특정 사용자의 상세 프로필 정보를 조회합니다.")
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @PathVariable Long id
    ) {
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}/diets") // 경로를 명확하게 지정
    @Operation(summary = "사용자 ID로 식단 이름 목록 조회", description = "제공된 사용자 ID를 사용하여 해당 사용자의 모든 식단 이름과 ID를 조회합니다.")
    public ResponseEntity<List<UserDietResponse>> getUserDiets(
            @Parameter(description = "조회할 사용자의 고유 ID", example = "1")
            @PathVariable Long id
    ) {
        List<UserDietResponse> userDiets = userService.getUserDietsById(id);
        return ResponseEntity.ok(userDiets);
    }
}
