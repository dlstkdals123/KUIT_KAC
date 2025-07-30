package org.example.kuit_kac.domain.home.dto;

import org.example.kuit_kac.domain.home.model.Level;

public class HomeCoachReportResponse {
    // 외식횟수, 공복시간 적음/많음 여부, 술자리 적음/많음 여부, 배달어플 빈도, 야식 적음/많음 여부
    private int diningOutNum; // 외식 횟수
    private Level fastingLevel; // 공복시간
    private Level dringkingLevel; // 술자리
    private Level deliveryLevel; // 배달어플 사용 빈도
    private Level lateNightLevel; // 야식
}
