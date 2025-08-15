package org.example.kuit_kac.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.onboarding.require")
public class AuthOnboardingProperties {
    private boolean require = true;
}
