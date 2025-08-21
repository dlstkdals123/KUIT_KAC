package org.example.kuit_kac.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth.onboarding.require")
public class AuthOnboardingProperties {
    /** 필수 약관 강제 여부 */
    private boolean require = true;
    public boolean isRequire() { return require; }
}
