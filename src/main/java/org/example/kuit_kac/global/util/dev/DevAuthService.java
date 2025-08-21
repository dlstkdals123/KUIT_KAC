package org.example.kuit_kac.global.util.dev;


import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DevAuthService {

    private final JwtProvider jwt;
    private final UserRepository userRepo;

    /**
     * uid 기준으로 DB를 조회해 kid/ver을 ‘DB에서’ 가져와 발급 (kid 파라미터 무시/검증)
     */
    public Map<String, Object> mintUserStrict(Long uid, String kid) {
        if (kid == null || kid.isBlank()) throw new CustomException(ErrorCode.ISSUE_TOKEN_KID);

        User uidUser = userRepo.findById(uid).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User kidUser = userRepo.findByKakaoId(kid).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // ✅ 클라가 kid를 보내더라도 DB와 ‘일치’해야만 발급
        if (kidUser != null && !(uidUser.getKakaoId()).equals(kid)) {
            throw new CustomException(ErrorCode.UID_KID_MISMATCH);
        }
        String access = jwt.generateUserAccessToken(uidUser.getId(), uidUser.getKakaoId());
        String refresh = jwt.generateRefreshToken(uidUser.getId());
        return Map.of(
                "access", access,
                "refresh", refresh,
                "stage", "user",
                "scope", "USER",
                "uid", uidUser.getId(),
                "kid", uidUser.getKakaoId()
        );
    }
}

