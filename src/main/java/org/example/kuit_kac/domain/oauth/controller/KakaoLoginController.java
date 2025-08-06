package org.example.kuit_kac.domain.oauth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KakaoLoginController {
    @GetMapping("/loginSuccess")
    @ResponseBody
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return "카카오 로그인 성공! 사용자 정보: " + oAuth2User.getAttributes();
    }
}
