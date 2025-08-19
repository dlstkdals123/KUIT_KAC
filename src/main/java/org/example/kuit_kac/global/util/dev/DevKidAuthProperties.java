package org.example.kuit_kac.global.util.dev;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "debug.kid-auth")
public class DevKidAuthProperties {
    /** 필터 토글 */
    private boolean enabled = true;
    /** HMAC 비밀키 */
    private String secret = "local-dev-only-key";
    /** 허용 시간 오차(초) */
    private long tsSkewSeconds = 300L;
}
