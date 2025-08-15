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
        // 카카오 유저 정보 JSON 데이터
        String kakaoId = String.valueOf(oAuth2User.getAttributes().get("id"));
        if (kakaoId == null) {
            throw new OAuth2AuthenticationException("Kakao 'id' not found in attributes");
        }

        // DB upsert: 없으면 생성, 있으면 조회
        User user = userService.findOrCreateByKakaoId(kakaoId);

        // 성공 핸들러에서 토큰 발급에 쓰도록 userId/kakaoId를 attributes에 넣어줌
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("kakaoId", kakaoId);
        attrs.put("userId", user.getId());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attrs, // attributes에 넣고
                "userId" // getName() 호출 시 나옴
        );
    }

}
