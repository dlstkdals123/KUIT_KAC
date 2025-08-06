package org.example.kuit_kac.domain.oauth.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.oauth.model.KakaoUser;
import org.example.kuit_kac.domain.oauth.repository.KakaoUserRepository;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user.repository.UserRepository;
import org.example.kuit_kac.domain.user.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 처리
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 카카오 유저 정보 JSON 데이터
        String kakaoId = String.valueOf(oAuth2User.getAttributes().get("id"));

        // DB에 해당 kakaoId로 등록된 유저가 없다면 생성
        User user = userService.findOrCreateByKakaoId(kakaoId);

        // OAuth2User 리턴 (세션 등록용)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("kakaoId", kakaoId), // attributes에 넣고
                "kakaoId" // 이걸 식별 키로 사용
        );
    }

}
