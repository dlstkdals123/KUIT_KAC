package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoachReportService {
    // TODO: 외식횟수, 공복시간 적음/적당/많음 여부, 술자리 적음/많음 여부, 배달어플 빈도, 야식 적음/많음 여부
    // 외식 횟수: 1주일간 1회 이하(적음), 2~3회(주의), 4회이상(위험) -> 어차피 횟수로 표시
    // 공복시간 적음(6시간미만), 적당(6~12시간), 많음(12시간)
    // 술자리: 1주일간 0회/1회(주의)/2회이상(위험) : 주의/위험 둘다 '잦은'으로 통일
    // 배달어플:  일단 0회, 1회, 2회로 하자
    // 야식: 일단 0회, 1회, 2회로 하자
    // 시간대를 datetime?으로 일주일로 설정

}
