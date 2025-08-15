package org.example.kuit_kac.domain.terms.web;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.terms.dto.*;
import org.example.kuit_kac.domain.terms.service.UserTermService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/terms")
public class UserTermsController {

    private final UserTermService userTermsService;

    /** 약관 동의/철회 업서트 */
    @PostMapping
    public ResponseEntity<List<TermAgreementResponse>> upsert(
            @PathVariable Long userId,
            @RequestBody TermAgreementUpsertRequest request) {
        return ResponseEntity.ok(userTermsService.upsertAgreements(userId, request));
    }

    /** 사용자 약관 전체 조회 */
    @GetMapping
    public ResponseEntity<List<TermAgreementResponse>> list(@PathVariable Long userId) {
        return ResponseEntity.ok(userTermsService.getAgreements(userId));
    }

    /** 요약(필수 동의 여부 포함) */
    @GetMapping("/summary")
    public ResponseEntity<TermSummaryResponse> summary(@PathVariable Long userId) {
        return ResponseEntity.ok(userTermsService.getSummary(userId));
    }
}
