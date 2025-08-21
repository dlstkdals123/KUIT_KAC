package org.example.kuit_kac.domain.ai.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.kuit_kac.domain.diet.service.DietService;
import org.example.kuit_kac.domain.food.model.FoodType;
import org.example.kuit_kac.domain.home.service.WeightService;
import org.example.kuit_kac.domain.user.model.User;
import org.example.kuit_kac.domain.user_information.service.UserInfoService;
import org.example.kuit_kac.global.util.DateRange;
import org.example.kuit_kac.domain.ai.dto.AiGenerateResponse;
import org.example.kuit_kac.domain.ai.dto.AiPlanGenerateRequest;
import org.example.kuit_kac.domain.diet.model.Diet;
import org.example.kuit_kac.domain.diet.model.DietType;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final UserInfoService userInfoService;
    private final WeightService weightService;
    private final DietService dietService;

    public String getSystemPrompt(User user, AiPlanGenerateRequest request) {
        // 필요한 정보 추출
        double weightValue = weightService.getLatestWeightByUserId(user.getId()).getWeight();

        // 프롬프트 파라미터 설정
        String gender = user.getGender().getValue();
        double bmr = user.getBMR(weightValue);
        String events = getEventsString(request);
        String exception = getExceptionString(user, request);
        String eating_out_type = userInfoService.getUserInformationByUserId(user.getId()).getEatingOutType();

        System.out.println("events: " + events);
        System.out.println("exception: " + exception);

        String systemPrompt = """
# GOALS
You are a certified AI nutritionist trained in clinical dietetics and human metabolism, with expertise equivalent to PhD-level training in nutritional science, sports performance nutrition, and medical nutrition therapy. Your task is to generate medically accurate, individualized 6-day meal plans based on user-specific goals and conditions.
Your core mission is to deliver meal plans that are:
- Nutritionally complete and balanced according to the latest guidelines (e.g., USDA Dietary Guidelines, WHO, EFSA, or local national standards)
- Aligned with the user's physiological needs, lifestyle factors, medical context, and personal food preferences
- Sustainable and realistic for long-term adherence

# NOTE
 INTERNALLY, To do so, you must infer from simple # QUERY:
- The user's primary dietary goal (e.g., weight loss, muscle gain, blood sugar control, GI recovery, etc.)
- Food preferences and intolerances, including habits (frequently eaten foods), allergies, dislikes, and repetition fatigue
- Event or context-specific constraints: such as physical activity patterns, competitions, fasting periods, or illness recovery timelines
- Psychosocial and behavioral factors: that influence compliance and meal timing (e.g., work schedules, energy dips, eating disorders)
- Comprehensive clinical protocols used in nutrition therapy (e.g., Mifflin-St Jeor for BMR, Harris-Benedict, AMDRs, AI and UL from Institute of Medicine, glycemic index/load considerations, etc.)
- Evidence-based adjustments from peer-reviewed nutrition research and sports physiology
- Practical food substitutions for budget, accessibility, and cultural fit
You always factor in micronutrient adequacy (vitamins & minerals), fiber and hydration needs, and avoid excessive intake of sodium, added sugar, or saturated fat. Macronutrient ratios are carefully adapted to user goals (e.g., carb-periodization for athletes, protein refeeding for sarcopenia, low-FODMAP for IBS contexts).

Let's work this out in a step by step way to be sure we have the right answer.

# QUERY
## User Info: Adult %s with a BMR of %.0f calories
* The user's primary dietary goal is: weight loss
- Options: weight loss, muscle gain, weight maintenance, blood sugar control, digestive recovery, etc.
Please adapt total caloric intake and macronutrient distribution accordingly:
- For weight loss: Target 15–25%% below estimated TDEE, with high-protein (30–40%% of kcal), low-fat (20–25%%), increased dietary fiber (≥25g/day)
- For muscle gain: Add 250–500 kcal above TDEE, emphasize high-protein (1.6–2.2 g/kg), and complex carbohydrates (45–55%% of kcal)
- For maintenance: Match TDEE, with macronutrients per AMDR (Carbs 45–65%%, Protein 10–35%%, Fat 20–35%%)
* Event %s, exception %s
- If applicable, describe relevant events (e.g., training, competition, travel, recovery):*
- Day before event: Emphasize hydration, low sodium, light digestion
- Event day: Omit [both event and exception meals], optimize other meals for stabilized energy and electrolyte support
- Day after event: Prioritize gut-friendly recovery foods—e.g., porridge, soft proteins, mineral-rich broths
* Preferred cuisine style: %s
- Incorporate elements of this cuisine for comfort and cultural fit while keeping nutritional integrity.*
- E.g., chicken breast, oatmeal, protein shakes—use nutritionally equivalent swaps.
* All meals must:
- Follow clinical nutrition standards (e.g., WHO, USDA DRI, EFSA, ADA)
- Provide 15–35g of protein per meal
- Include at least one vegetable side per meal
- Avoid excessive sodium, added sugar, and saturated fats
- Reflect adequate hydration and micronutrient diversity
- Be easily digestible at dinner time
* INTERNAL RULES (DO NOT ASK USER DIRECTLY)
Respect implicit exclusions (e.g., fatigue from repeated meals, known allergies, previously flagged food aversions) even if not stated.
* Integrate evidence-based clinical logic:
- Use Mifflin-St Jeor or Harris-Benedict for energy expenditure
- Apply AMDRs, glycemic index/load, and clinical adaptations (e.g., low-FODMAP, DASH, Mediterranean diet) when fitting
- Substitute for budget, availability, and cultural comfort where needed
    """;
        return systemPrompt.formatted(gender, bmr, events, exception, eating_out_type);
    }

    public String getUserPrompt(AiPlanGenerateRequest request) {
        List<DateRange> dateRanges = getDateRanges(request);
        int dayCount = calculateTotalDays(dateRanges);

        System.out.println("dayCount: " + dayCount);
        
        String userPrompt = """
        ## Each food item must include:
- `name`: food name/ `food_type`: one of food type enum/ `is_processed_food`: boolean value/ `kcal`: number of kcal/ `protein`: grams of protein/ `fat`: grams of fat/ `carb`: grams of carbohydrates/ `sugar`: grams of sugar/ `score`: 0 or 1 or 2/ `unit_type`: portion unit (e.g., "그릇", "개", "팩")/ `unit_num`: weight or volume per unit (e.g., 600 for grams or ml)
- `food_type` is one of the following:
NORMAL_GRAIN_AND_TUBER, NORMAL_FRUIT, NORMAL_GRILLED, NORMAL_SOUP_AND_TANG, NORMAL_KIMCHI, NORMAL_NAMUL_AND_SUKCHAE, NORMAL_BEANS_AND_NUTS, NORMAL_NOODLE_AND_DUMPLING, NORMAL_RICE, NORMAL_STIR_FRIED, NORMAL_BREAD_AND_SNACK, NORMAL_FRESH_AND_MUCHIM, NORMAL_FISH_AND_MEAT, NORMAL_DAIRY_AND_ICECREAM, NORMAL_BEVERAGE_AND_TEA, NORMAL_SAUCE_AND_SEASONING, NORMAL_PICKLE, NORMAL_PANCAKE, NORMAL_SALTED_SEAFOOD, NORMAL_BRAISED, NORMAL_PORRIDGE_AND_SOUP, NORMAL_STEW_AND_HOT_POT, NORMAL_STEAMED, NORMAL_VEGETABLE_AND_SEAWEED, NORMAL_FRIED, PROCESSED_BREAD_AND_SNACK, PROCESSED_OTHER_FOOD, PROCESSED_AGRICULTURAL, PROCESSED_SUGAR, PROCESSED_ANIMAL, PROCESSED_TOFU_AND_MUK, PROCESSED_NOODLE, PROCESSED_ICECREAM, PROCESSED_SEAFOOD, PROCESSED_OIL, PROCESSED_MEAT, PROCESSED_EGG, PROCESSED_DAIRY, PROCESSED_BEVERAGE, PROCESSED_SAUCE, PROCESSED_JAM, PROCESSED_PICKLE_AND_BRAISED, PROCESSED_SEASONING, PROCESSED_ALCOHOL, PROCESSED_INSTANT, PROCESSED_COCOA_CHOCOLATE, PROCESSED_NUTRITION, PROCESSED_MEDICAL

## Example JSON Output:
<EXJSON>
  "day1": {
    "아침": [
      {
        "name": "현미죽",
        "food_type": "NORMAL_RICE",
        "is_processed_food": false,
        "kcal": 200,
        "protein": 6.0,
        "fat": 1.0,
        "carb": 40.0,
        "sugar": 5.0,
        "unit_type": "그릇",
        "unit_num": 400,
        "score": 1
      }
    ],
    "점심": [
       ...
    ]
  }
</EXJSON>

# REMEMBER:
* Use Korean food names only
* DO NOT include markdown, comments, explanations, or non-JSON content
* Return a valid JSON object with %d keys: "day1" to "day%d"
* Each key contains 3 sub-keys: "아침", "점심", "저녁"
* Each meal is a list of 1–4 food items, or "[]" if skipped


Think about this logically.
    """;
        return userPrompt.formatted(dayCount, dayCount);
    }

    public AiGenerateResponse validateResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Map<String, List<AiGenerateResponse.FoodItem>>> responseMap = 
                objectMapper.readValue(response, 
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Map<String, List<AiGenerateResponse.FoodItem>>>>() {}
                );
            
            // 유효성 검사 추가
            validateResponseStructure(responseMap);
            
            return new AiGenerateResponse(responseMap);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.GPT_API_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GPT_API_ERROR);
        }
    }

    private void validateResponseStructure(Map<String, Map<String, List<AiGenerateResponse.FoodItem>>> responseMap) {
        for (Map.Entry<String, Map<String, List<AiGenerateResponse.FoodItem>>> dayEntry : responseMap.entrySet()) {
            String day = dayEntry.getKey();
            Map<String, List<AiGenerateResponse.FoodItem>> meals = dayEntry.getValue();
            
            for (Map.Entry<String, List<AiGenerateResponse.FoodItem>> mealEntry : meals.entrySet()) {
                String dietType = mealEntry.getKey();
                List<AiGenerateResponse.FoodItem> foods = mealEntry.getValue();
                
                // dietType 검증 - DietType enum의 koreanName과 비교
                try {
                    DietType.getDietType(dietType);
                } catch (CustomException e) {
                    throw new CustomException(ErrorCode.GPT_API_ERROR);
                }
                
                // foodType 검증 - FoodType enum의 value와 비교
                for (AiGenerateResponse.FoodItem food : foods) {
                    try {
                        FoodType.getFoodType(food.food_type());
                    } catch (CustomException e) {
                        throw new CustomException(ErrorCode.GPT_API_ERROR);
                    }
                }
            }
        }
    }

    private List<DateRange> getDateRanges(AiPlanGenerateRequest request) {
        // 활동 날짜들을 기준으로 전/후 1일씩 포함하는 날짜 범위 계산
        List<LocalDate> activityDates = request.dietActivities().stream()
                .map(activity -> activity.dietDate())
                .sorted()
                .toList();

        // 연속된 날짜 그룹으로 분리 (전후 1일씩 포함하여 최대한 연속으로 묶기)
        List<DateRange> dateRanges = new ArrayList<>();
        LocalDate rangeStart = activityDates.get(0).minusDays(1);
        LocalDate rangeEnd = activityDates.get(0).plusDays(1);

        for (int i = 1; i < activityDates.size(); i++) {
            LocalDate currentDate = activityDates.get(i);
            LocalDate currentStart = currentDate.minusDays(1);
            LocalDate currentEnd = currentDate.plusDays(1);
            
            // 현재 범위와 겹치거나 연속되는지 확인
            if (currentStart.minusDays(1).isAfter(rangeEnd)) {
                // 겹치지 않으면 이전 범위 저장하고 새로운 범위 시작
                dateRanges.add(new DateRange(rangeStart, rangeEnd));
                rangeStart = currentStart;
                rangeEnd = currentEnd;
            } else {
                // 겹치거나 연속되면 범위 확장
                rangeEnd = currentEnd;
            }
        }
        // 마지막 범위 추가
        dateRanges.add(new DateRange(rangeStart, rangeEnd));
        
        return dateRanges;
    }

    private int calculateTotalDays(List<DateRange> dateRanges) {
        return dateRanges.stream()
                .mapToInt(range -> (int) range.start().until(range.end().plusDays(1), java.time.temporal.ChronoUnit.DAYS))
                .sum();
    }

    private List<LocalDate> getAllDatesInOrder(AiPlanGenerateRequest request) {
        List<DateRange> dateRanges = getDateRanges(request);
        List<LocalDate> allDates = new ArrayList<>();
        
        // 모든 날짜를 순서대로 나열
        for (DateRange range : dateRanges) {
            LocalDate current = range.start();
            while (!current.isAfter(range.end())) {
                allDates.add(current);
                current = current.plusDays(1);
            }
        }
        
        return allDates;
    }

    private String getEventsString(AiPlanGenerateRequest request) {
        if (request.dietActivities().isEmpty()) {
            return "none";
        }

        List<LocalDate> allDates = getAllDatesInOrder(request);
        List<String> events = new ArrayList<>();

        // 각 활동 날짜에 대해 Day 번호와 식사 타입을 조합
        for (var activity : request.dietActivities()) {
            LocalDate activityDate = activity.dietDate();
            int dayIndex = allDates.indexOf(activityDate) + 1; // 1부터 시작하는 인덱스
            
            // 식사 타입을 한글로 변환
            String dietType = activity.dietType();
            
            events.add("Day" + dayIndex + "-" + dietType);
        }

        // Day 번호들을 문자열로 변환
        return events.stream()
                .collect(Collectors.joining(", "));
    }

    private String getExceptionString(User user, AiPlanGenerateRequest request) {
        if (request.dietActivities().isEmpty()) {
            return "none";
        }

        List<LocalDate> allDates = getAllDatesInOrder(request);
        
        // 해당 사용자의 dateRanges 기간 내 모든 식사 기록 조회
        List<Diet> userDiets = new ArrayList<>();
        for (DateRange dateRange : getDateRanges(request)) {
            userDiets.addAll(dietService.getPlansByUserIdBetweenDietDate(user.getId(), dateRange.start(), dateRange.end()));
        }
        
        if (userDiets.isEmpty()) {
            return "none";
        }

        List<String> exceptions = new ArrayList<>();
        int dayIndex = 1;

        // 각 범위를 순서대로 순회하면서 식사 기록이 있는지 확인
        for (LocalDate currentDate : allDates) {
            // 현재 날짜의 식사 기록들 찾기
            List<Diet> dayDiets = userDiets.stream()
                    .filter(diet -> diet.getDietDate().equals(currentDate))
                    .toList();
            
            // 각 식사 타입별로 확인
            for (Diet diet : dayDiets) {
                String mealType = diet.getDietType().getKoreanName();
                exceptions.add("Day" + dayIndex + "-" + mealType);
            }
            
            dayIndex++;
        }

        return exceptions.isEmpty() ? "none" : String.join(", ", exceptions);
    }

    public String convertDayToDate(String dayResponse, AiPlanGenerateRequest request) {
        List<LocalDate> allDates = getAllDatesInOrder(request);
        
        // Day1, Day2... 를 실제 날짜로 치환
        String response = dayResponse;
        for (int i = 0; i < allDates.size(); i++) {
            String dayPattern = "day" + (i + 1);
            String dateStr = allDates.get(i).toString();
            response = response.replaceAll(dayPattern, dateStr);
        }
        
        return response;
    }
}