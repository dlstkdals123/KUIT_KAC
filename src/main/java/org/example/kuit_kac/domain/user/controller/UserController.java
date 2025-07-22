package org.example.kuit_kac.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "사용자 관리", description = "사용자 정보 조회, 생성, 수정, 삭제 등 사용자 관련 모든 기능을 제공합니다.")
@RequiredArgsConstructor
public class UserController {
}
