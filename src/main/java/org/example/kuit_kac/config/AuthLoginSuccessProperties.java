package org.example.kuit_kac.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Getter
@Service
@ConfigurationProperties(prefix = "auth.login-success")
public class AuthLoginSuccessProperties {
    private String mode; // JSON or DEEPLINK
    private String deepLink; // myapp://login
    private String accessParam; // accessToken
    private String refreshParam; // refreshToken

    public boolean isDeepLink() { // 딥링크 상태 체크
        return "DEEPLINK".equalsIgnoreCase(mode);
    }
}
