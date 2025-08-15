package org.example.kuit_kac.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Getter
@ConfigurationProperties(prefix = "auth.login-success")
public class AuthLoginSuccessProperties {
    public enum Mode {JSON, DEEPLINK}

    private Mode mode = Mode.DEEPLINK; // JSON or DEEPLINK
    private String deepLink; // myapp://login
    private String accessParam = "access_token"; // accessToken
    private String refreshParam = "refresh_token"; // refreshToken
    private String expiresParam = "expires_in";
    private String stateParam = "state";
    private String onboardingParam = "onboarding_required";
    private long accessTtlSeconds = 1800;


    public boolean isDeepLink() {
        return mode == Mode.DEEPLINK;
    }

    public boolean requireOnboarding = true;

}
