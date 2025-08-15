package org.example.kuit_kac.domain.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
public class KakaoOauth2User implements OAuth2User {
    private final String kakaoId;
    private final Map<String, Object> attributes;

    private final Set<GrantedAuthority> authorities =
            Set.of(new SimpleGrantedAuthority("ROLE_USER"));

    // Spring Security가 내부적으로 식별자 필요할 때 사용
    @Override
    public String getName() {
        return kakaoId;
    }
}
