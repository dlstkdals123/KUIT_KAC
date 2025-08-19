package org.example.kuit_kac.domain.terms.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.terms.dto.*;
import org.example.kuit_kac.domain.terms.service.UserTermsService;
import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.exception.ErrorResponse;
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
            description = "특정 사용자의 약관 동의 상태를 생성/갱신합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TermAgreementUpsertRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "동의 예시",
                                            value = "{ \"agreements\": [ { \"code\": \"SERVICE_TOS\", \"version\": \"v1.2\", \"agreed\": true }, { \"code\": \"PRIVACY\", \"version\": \"v3.0\", \"agreed\": true } ] }"
                                    ),
                                    @ExampleObject(
                                            name = "철회 예시",
                                            value = "{ \"agreements\": [ { \"code\": \"MARKETING\", \"version\": \"v1.0\", \"agreed\": false } ] }"
                                    )
                            }
                    )
            )
    )
    @Parameters({
            @Parameter(name = "userId", in = ParameterIn.PATH, required = true, description = "대상 사용자 ID", example = "3")
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "업데이트 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TermAgreementResponse.class)),
                            examples = @ExampleObject(
                                    value = "[{\"userId\":3,\"code\":\"SERVICE_TOS\",\"version\":\"v1.2\",\"agreed\":true,\"agreedAt\":\"2025-08-15T12:30:45\",\"withdrawnAt\":null}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인 외 사용자의 약관 변경 시도",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\":\"AUTH_004\",\"message\":\"forbidden\",\"timestamp\":\"2025-08-15T12:30:45\"}"
                            )
                    )
            )
    })
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

    @Operation(summary = "사용자 약관 전체 조회",
            description = "특정 사용자의 모든 약관 동의 내역을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "userId", in = ParameterIn.PATH, required = true, example = "3")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TermAgreementResponse.class)),
                    examples = @ExampleObject(value = """
                            [
                              {"userId":3,"code":"SERVICE_TOS","version":"v1.2","agreed":true,"agreedAt":"2025-08-15T12:30:45","withdrawnAt":null},
                              {"userId":3,"code":"PRIVACY","version":"v3.0","agreed":true,"agreedAt":"2025-08-15T12:30:45","withdrawnAt":null}
                            ]
                            """)))
    @GetMapping
    public ResponseEntity<List<TermAgreementResponse>> list(
            @Parameter(name = "userId", in = ParameterIn.PATH, required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userTermsService.getAgreements(userId));
    }

    @Operation(summary = "약관 요약 조회",
            description = "필수 약관 동의 여부를 포함한 요약 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "userId", in = ParameterIn.PATH, required = true, example = "3")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TermSummaryResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "userId": 3,
                              "serviceTosAgreed": true,
                              "privacyAgreed": true,
                              "marketingAgreed": false,
                              "requiredAllAgreed": true
                            }
                            """)))
    @GetMapping("/summary")
    public ResponseEntity<TermSummaryResponse> summary(
            @Parameter(name = "userId", in = ParameterIn.PATH, required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userTermsService.getSummary(userId));
    }
}
