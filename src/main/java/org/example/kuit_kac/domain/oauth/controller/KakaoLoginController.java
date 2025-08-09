package org.example.kuit_kac.domain.oauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.global.util.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.StringTokenizer;

@Controller
@RequiredArgsConstructor
public class KakaoLoginController {
    private final JwtProvider jwtProvider;

//    @GetMapping("/loginSuccess")
//    @ResponseBody
//    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
//        String kakaoId = (String) oAuth2User.getAttributes().get("kakaoId");
//        if (kakaoId == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Kakao Id not found"));
//        }
//        String token = jwtProvider.generateToken(kakaoId);
//        return ResponseEntity.ok(Map.of("token", token)); // JSON 형태로 응답
//    }

}
