package org.example.kuit_kac.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "auth.login.success")
@Slf4j
public class AuthLoginSuccessProperties {
    public enum Mode {JSON, DEEPLINK}

    private Mode mode = Mode.DEEPLINK; // JSON or DEEPLINK
    private String deepLink; // myapp://login
    private String accessParam = "access_token"; // accessToken
    private String refreshParam = "refresh_token"; // refreshToken
    private String expiresParam = "expires_in";
    private String stateParam = "state";
    private String onboardingRequired = "onboarding_required";
    private long accessTtlSeconds = 1800;


    public boolean isDeepLink() {
        return mode == Mode.DEEPLINK;
    }

    public boolean requireOnboarding = true;

    // 임시 로그
    @PostConstruct
    public void _log() {
        log.info("[Props] mode={}, deepLink='{}', onboardingParam='{}'",
                mode, deepLink, onboardingRequired);
    }

}
