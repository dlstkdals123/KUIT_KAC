package org.example.kuit_kac.domain.terms.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.terms.dto.*;
import org.example.kuit_kac.domain.terms.model.*;
import org.example.kuit_kac.domain.terms.repository.UserTermAgreementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserTermService {

    private final UserTermAgreementRepository repo;

    @Transactional
    public List<TermAgreementResponse> upsertAgreements(Long userId, TermAgreementUpsertRequest request) {
        LocalDateTime now = LocalDateTime.now();
        List<TermAgreementResponse> result = new ArrayList<>();

        for (TermAgreementItem item : request.getAgreements()) {
            UserTermAgreementId id = new UserTermAgreementId(userId, item.getCode());
            UserTermAgreement entity = repo.findById(id).orElse(
                    UserTermAgreement.builder()
                            .id(id)
                            .version(item.getVersion())
                            .agreed(false)
                            .build()
            );

            if (item.isAgreed()) {
                entity.agree(item.getVersion(), now);
            } else {
                // 철회 처리
                entity.withdraw(now);
            }
            repo.save(entity);

            result.add(TermAgreementResponse.builder()
                    .userId(userId)
                    .code(item.getCode())
                    .version(entity.getVersion())
                    .agreed(entity.isAgreed())
                    .agreedAt(entity.getAgreedAt())
                    .withdrawnAt(entity.getWithdrawnAt())
                    .build());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<TermAgreementResponse> getAgreements(Long userId) {
        List<UserTermAgreement> list = repo.findByIdUserId(userId);
        List<TermAgreementResponse> result = new ArrayList<>();
        for (UserTermAgreement e : list) {
            result.add(TermAgreementResponse.builder()
                    .userId(userId)
                    .code(e.getId().getCode())
                    .version(e.getVersion())
                    .agreed(e.isAgreed())
                    .agreedAt(e.getAgreedAt())
                    .withdrawnAt(e.getWithdrawnAt())
                    .build());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public TermSummaryResponse getSummary(Long userId) {
        boolean s = repo.isAgreed(userId, TermCode.SERVICE_TOS);
        boolean p = repo.isAgreed(userId, TermCode.PRIVACY);
        boolean m = repo.isAgreed(userId, TermCode.MARKETING);

        return TermSummaryResponse.builder()
                .userId(userId)
                .serviceTosAgreed(s)
                .privacyAgreed(p)
                .marketingAgreed(m)
                .requiredAllAgreed(s && p)
                .build();
    }

    /** 로그인/필터에서 쓸: 필수 약관 동의 여부 */
    @Transactional(readOnly = true)
    public boolean hasAgreedRequired(Long userId) {
        return repo.isAgreed(userId, TermCode.SERVICE_TOS)
                && repo.isAgreed(userId, TermCode.PRIVACY);
    }
}
