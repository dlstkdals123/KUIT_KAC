package org.example.kuit_kac.domain.oauth.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> origin = oAuth2User.getAttributes();

        // 1) kakao id 안전 추출
        Object idObj = origin.get("id");
        if (idObj == null) {
            throw new OAuth2AuthenticationException("Kakao 'id' not found in attributes");
        }
        String kakaoId = String.valueOf(idObj);

        // 2) 원본 attrs 보존 + 우리가 쓸 필드 보강
        Map<String, Object> attrs = new HashMap<>(origin);
        attrs.put("kakaoId", kakaoId);

        // 3) nameAttributeKey는 실제 존재하는 키로(여기선 kakaoId)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attrs,
                "kakaoId"
        );
    }

}
