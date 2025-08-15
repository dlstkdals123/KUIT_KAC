package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.service.DietFoodService;
import org.example.kuit_kac.domain.home.dto.HomeCoachReportResponse;
import org.example.kuit_kac.domain.home.model.Level;
import org.example.kuit_kac.global.util.TimeRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CoachReportService {
    // TODO: 외식횟수, 공복시간 적음/적당/많음 여부, 술자리 적음/많음 여부, 배달어플 빈도, 야식 적음/많음 여부
    // 외식 횟수: 1주일간 1회 이하(적음), 2~3회(주의), 4회이상(위험) -> 어차피 횟수로 표시
    // 공복시간 적음(6시간미만), 적당(6~12시간), 많음(12시간)
    // 술자리: 1주일간 0회/1회(주의)/2회이상(위험) : 주의/위험 둘다 '잦은'으로 통일
    // 배달어플:  일단 0회, 1회, 2회로 하자
    // 야식: 일단 0회, 1회, 2회로 하자

    DietService dietService;
    DietFoodService dietFoodService;

    @Transactional(readOnly = true)
    public HomeCoachReportResponse getCoachReport(Long userId) {
        TimeRange range = TimeRange.getPastWeekTimeRange();

        // 일주일 외식 횟수 가져오기
        long diningOutCount = dietService.countDietFoodWithConditions(userId, DietEntryType.DINING_OUT, false, range);

        // 공복시간 평균 계산
        Level fastingLevel = calculateWeeklyFastingLevel(userId, range);

        // 일주일 술자리 횟수 가져오기
        long drinkingCount = dietService.countDietFoodWithConditions(userId, DietEntryType.DRINKING, false, range);
        Level dringkingLevel = (drinkingCount == 0) ? Level.LOW
                : (drinkingCount == 1) ? Level.NORMAL
                : Level.HIGH;

        // TODO 배달어플(로컬데이터 필요)
        Level deliveryLevel = Level.HIGH;

        // 일주일 야식 횟수 가져오기
        long lateNightCount = dietService.countDietFoodWithConditions(userId, null, true, range);
        Level lateNightLevel = (lateNightCount == 0) ? Level.LOW
                : (lateNightCount == 1) ? Level.NORMAL
                : Level.HIGH;

        return new HomeCoachReportResponse(
                diningOutCount, // 외식횟수
                fastingLevel, // 공복시간
                dringkingLevel, // 술자리
                deliveryLevel, // 배달어플
                lateNightLevel // 야식
        );
    }


    public Level calculateWeeklyFastingLevel(Long userId, TimeRange range) {
        // 1. 일주일간 RECORD 식사기록만 가져와 시간순 정렬
        List<LocalDateTime> recordTimes = dietFoodService.getDietFoodsByDietIdAndTimeRange(userId, range.start(), range.end())
                .stream()
                .filter(df -> df.getDiet().getDietEntryType() == DietEntryType.RECORD)
                .map(DietFood::getDietTime)
                .sorted()
                .toList();

        List<Long> fastingDurations = new ArrayList<>();

        // 2. 인접한 RECORD 식사들 사이 공복시간 계산
        for (int i = 1; i < recordTimes.size(); i++) {
            long fastingHours = Duration.between(recordTimes.get(i - 1), recordTimes.get(i)).toHours();
            if (fastingHours > 0 && fastingHours < 24) {
                fastingDurations.add(fastingHours);
            }
        }

        // 3. 공복시간이 없으면 LOW 리턴
        if (fastingDurations.isEmpty()) return Level.LOW;

        // 4. 평균 공복시간 계산 후 등급 리턴
        double avg = fastingDurations.stream().mapToLong(Long::longValue).average().orElse(0);
        if (avg < 6) return Level.LOW;
        else if (avg < 12) return Level.NORMAL;
        else return Level.HIGH;
    }
}
