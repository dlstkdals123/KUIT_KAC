package org.example.kuit_kac.domain.terms.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.terms.dto.*;
import org.example.kuit_kac.domain.terms.service.UserTermsService;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/terms")
@Tag(name = "약관", description = "사용자 약관 동의 API")
public class UserTermsController {

    private final UserTermsService userTermsService;
    private final UserService userService;

    @Operation(
            summary = "약관 동의/철회 업서트",
            description = "특정 사용자에 대해 약관 동의/철회 상태를 업데이트합니다."
    )
    @ApiResponse(responseCode = "200", description = "업데이트 성공")
//    @ApiResponse(responseCode = "403", description = "다른 사용자의 약관을 수정하려고 함")
    @PostMapping
    public ResponseEntity<?> upsertTerms(
            @Parameter(name = "userId", in = ParameterIn.PATH, required = true) @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal p,
            @RequestBody TermAgreementUpsertRequest req) {
        // uid가 있으면 uid == path userId 확인
        if (p.getUserId() != null && !p.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "forbidden"));
        }
        // uid가 없으면 kid로 사용자 찾아서 id 비교
        if (p.getUserId() == null) {
            Long ownerId = userService.findIdByKakaoId(p.getKakaoId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            if (!ownerId.equals(userId)) {
                return ResponseEntity.status(403).body(Map.of("error", "forbidden"));
            }
        }
        // 통과 시 처리
        var res = userTermsService.upsertAgreements(userId, req);
        return ResponseEntity.ok(res);
    }

    @Operation(
            summary = "사용자 약관 전체 조회",
            description = "특정 사용자가 동의한 전체 약관 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<TermAgreementResponse>> list(
            @Parameter(name = "userId", in = ParameterIn.PATH, required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userTermsService.getAgreements(userId));
    }

    @Operation(
            summary = "약관 요약 조회",
            description = "필수 약관 동의 여부를 포함한 약관 요약 정보를 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/summary")
    public ResponseEntity<TermSummaryResponse> summary(
            @Parameter(name = "userId", in = ParameterIn.PATH, required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userTermsService.getSummary(userId));
    }
}
