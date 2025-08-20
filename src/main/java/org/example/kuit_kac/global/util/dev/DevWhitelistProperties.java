package org.example.kuit_kac.global.util.dev;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "debug.kid-auth.whitelist")
@Slf4j
public class DevWhitelistProperties {
    private boolean enabled = false;
    private List<String> kids = new ArrayList<>();

    /** 화이트리스트 판정 */
    public boolean allows(String kid) {
        if (kid == null || kid.isBlank()) return false;
        // enabled=false 면 화이트리스트 검증을 건너뛰고 모두 허용(DEV 편의)
        return enabled ? kids.contains(kid) : true;
    }

    @PostConstruct
    void logBinding() {
        // 1) sanitize: null/공백 제거 + trim + 중복 제거
        List<String> sanitized = kids == null ? List.of() :
                kids.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .collect(Collectors.toList());
        this.kids = new ArrayList<>(sanitized);

        // 2) 마스킹: 앞 2자리만 노출
        List<String> masked = sanitized.stream()
                .map(DevWhitelistProperties::maskKid)
                .collect(Collectors.toList());

        // 3) 요약 로그
        log.info("[DevWhitelist] enabled={} boundCount={} kidsMasked={}",
                enabled, sanitized.size(), masked);

        // 4) 경고: 한 개도 바인딩 안 됐을 때
        if (sanitized.isEmpty()) {
            log.warn("[DevWhitelist] No kids are bound. " +
                    "Check your YAML/env. Values like ${ANDROID_MH} empty -> becomes blank. " +
                    "If you want to hardcode, use quotes: kids: [\"4384440657\"]");
        }
    }

    private static String maskKid(String k) {
        if (k == null || k.isEmpty()) return "(empty)";
        if (k.length() <= 2) return k.charAt(0) + "*";
        return k.substring(0, 2) + "***" + k.substring(k.length()-3, k.length()-1);
    }



}
