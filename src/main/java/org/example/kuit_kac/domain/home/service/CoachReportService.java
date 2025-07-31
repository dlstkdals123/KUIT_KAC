package org.example.kuit_kac.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.model.DietEntryType;
import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.diet_food.model.DietFood;
import org.example.kuit_kac.domain.diet_food.repository.DietFoodRepository;
import org.example.kuit_kac.domain.home.dto.HomeCoachReportResponse;
import org.example.kuit_kac.domain.home.model.Level;
import org.example.kuit_kac.global.util.TimeRange;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    DietService dietService;
    DietFoodRepository dietFoodRepository;

    public HomeCoachReportResponse getCoachReport(Long userId) {
        TimeRange range = TimeRange.getPastWeekDietTimeRange();

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


    private Level calculateWeeklyFastingLevel(Long userId, TimeRange range) {
        // 1. 일주일 식사기록 전부 가져오기
        List<DietFood> dietFoods = dietFoodRepository
                .findByDietUserIdAndDietTimeBetween(userId, range.start(), range.end())
                .stream()
                .sorted(Comparator.comparing(DietFood::getDietTime)) // 시간순 정렬
                .toList();
        // 2. 날짜별로 묶기(LocalDate 기준)
        // TODO: 전날 기록 없으면 계산에 포함 X
        // TODO: 7일전 저녁 계산에 포함 여부?
        Map<LocalDate, List<DietFood>> dietsByDay = dietFoods.stream()
                .collect(Collectors.groupingBy(dietFood -> {
                    LocalDateTime time = dietFood.getDietTime();
                    // 새벽 3시 이전이면 전날로 취급
                    if (time.getHour() < 3) {
                        return time.toLocalDate().minusDays(1);
                    } else {
                        return time.toLocalDate();
                    }
                }));
        List<Long> allFastingDurations = new ArrayList<>();

        // 3. 공복값을 수집하고 평균 계산
        for (LocalDate date : dietsByDay.keySet()) {
            List<LocalDateTime> times = dietsByDay.get(date).stream() // 해당하는 날의 식단들 가져옴
                    .map(DietFood::getDietTime)
                    .sorted()
                    .toList();

            if (times.size() < 2) continue; // 공복시간 구할 식사 2개 이상 필요

            // 현재 날짜에 아침, 점심, 저녁이 있다고 가정하고 구간별 공복 시간 계산(간식도 있으면 포함됨)
            for (int i = 1; i < times.size(); i++) {
                long fastingHours = Duration.between(times.get(i - 1), times.get(i)).toHours();
                allFastingDurations.add(fastingHours);
            }

            // 전날 마지막 식사와 오늘 아침 사이 공복 계산
            // TODO: 전날 식사 없으면 공복계산 X?
            LocalDate previousDate = date.minusDays(1);
            if (dietsByDay.containsKey(previousDate)) {
                List<LocalDateTime> prevTimes = dietsByDay.get(previousDate).stream()
                        .map(DietFood::getDietTime)
                        .sorted()
                        .toList();
                LocalDateTime lastDietPrevDay = prevTimes.get(prevTimes.size() - 1); // 전날 마지막 식사시간
                LocalDateTime firstDietPrevDay = times.get(0); // 오늘 아침 식사시간

                long overnightFasting = Duration.between(lastDietPrevDay, firstDietPrevDay).toHours();
                if (overnightFasting > 0 && overnightFasting < 24) {
                    allFastingDurations.add(overnightFasting);
                }
            }
        }
        // TODO: 공복시간 아예없으면 에러처리해야함
        if (allFastingDurations.isEmpty()) return Level.LOW;

        double avgFasting = allFastingDurations.stream().mapToLong(Long::longValue).average().orElse(0);

        // 4. 평균에 따라 enum 리턴
        if (avgFasting < 6) return Level.LOW;
        else if (avgFasting < 12) return Level.NORMAL;
        else return Level.HIGH;
    }
}
