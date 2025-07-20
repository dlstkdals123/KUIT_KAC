-- 사용자 관련 테이블
INSERT INTO `user` (`nickname`, `password`, `email`, `gender`, `age`, `height`, `target_weight`) VALUES
('user1', 'hashed_password_1', 'user1@example.com', 'MALE', 30, 175, 70.5),
('user2', 'hashed_password_2', 'user2@example.com', 'FEMALE', 25, 163, 55.0);

INSERT INTO `user_information` (`user_id`, `has_diet_experience`, `diet_fail_reason`, `appetite_type`, `weekly_eating_out_count`, `eating_out_type`, `diet_velocity`) VALUES
(1, TRUE, '야식', 'BIG', '2-3', 'KOREAN', 'COACH');

INSERT INTO `weight` (`user_id`, `weight`) VALUES
(1, 85.0),
(2, 60.7);


-- 음식 관련 테이블
INSERT INTO `diet` (`user_id`, `diet_type`, `diet_date`) VALUES
(1, 'RECORD', CURDATE()),
(1, 'PLAN', CURDATE()),
(1, 'AI_PLAN', CURDATE()),
(1, 'AI_PLAN', CURDATE() - INTERVAL 1 DAY),
(1, 'AI_PLAN', CURDATE() - INTERVAL 2 DAY),
(1, 'AI_PLAN', CURDATE() - INTERVAL 3 DAY),
(1, 'AI_PLAN', CURDATE() - INTERVAL 4 DAY),
(1, 'FASTING', CURDATE()),
(1, 'DINING_OUT', CURDATE() - INTERVAL 1 DAY),
(1, 'DINING_OUT', CURDATE() - INTERVAL 3 DAY),
(1, 'DRINKING', CURDATE() - INTERVAL 3 DAY),
(2, 'RECORD', CURDATE()),
(2, 'PLAN', CURDATE()),
(2, 'AI_PLAN', CURDATE()),
(2, 'FASTING', CURDATE()),
(2, 'DINING_OUT', CURDATE()),
(2, 'DRINKING', CURDATE());

INSERT INTO `meal` (`user_id`, `diet_id`, `name`, `meal_type`, `meal_time`) VALUES
(1, 1, '오늘의 아침 기록', 'BREAKFAST', CONCAT(CURDATE(), ' 09:00:00')),
(1, 1, '오늘의 점심 기록', 'LUNCH', CONCAT(CURDATE(), ' 13:00:00')),
(1, 2, '오늘의 저녁 계획', 'DINNER', NULL),
(1, 3, 'AI 계획 아침1', 'BREAKFAST', NULL),
(1, 3, 'AI 계획 점심1', 'LUNCH', NULL),
(1, 4, 'AI 계획 아침2', 'BREAKFAST', NULL),
(1, 5, 'AI 계획 아침3', 'BREAKFAST', NULL),
(1, 6, 'AI 계획 아침4', 'BREAKFAST', NULL),
(1, 7, 'AI 계획 아침5', 'BREAKFAST', NULL),
(1, NULL, '나만의 아침', 'TEMPLATE', NULL),
(1, NULL, '나만의 점심', 'TEMPLATE', NULL),
(1, NULL, '나만의 저녁', 'TEMPLATE', NULL);

INSERT INTO `food` (`name`, `unit_type`, `unit_num`, `food_type`, `is_processed_food`, `calorie`, `carbohydrate`, `protein`, `fat`, `sugar`) VALUES
('국밥_돼지머리', '그릇', 500, 'NORMAL_RICE', FALSE, 137.0, 6.7, 5.16, 15.94, 0.16),
('국밥_순대국밥', '그릇', 500, 'NORMAL_RICE', FALSE, 75.0, 3.17, 2.28, 10.38, 0.17),
('가래떡', '개', 100, 'NORMAL_BREAD_AND_SNACK', FALSE, 195.0, 3.92, 0.51, 43.73, 0.0),
('경단_깨', '개', 50, 'NORMAL_BREAD_AND_SNACK', FALSE, 240.0, 6.28, 6.72, 38.58, 0.61),
('간자장', '그릇', 500, 'NORMAL_NOODLE_AND_DUMPLING', FALSE, 125.0, 5.12, 4.89, 15.13, 0.86),
('국수_막국수', '그릇', 500, 'NORMAL_NOODLE_AND_DUMPLING', FALSE, 133.0, 5.8, 1.61, 23.9, 4.88),
('닭죽', '그릇', 300, 'NORMAL_PORRIDGE_AND_SOUP', FALSE, 44.0, 2.68, 0.57, 7.14, 0.0),
('스프_양송이버섯', '그릇', 300, 'NORMAL_PORRIDGE_AND_SOUP', FALSE, 105.0, 2.41, 7.04, 8.06, 2.52),
('갈비탕', '그릇', 500, 'NORMAL_SOUP_AND_TANG', FALSE, 54.0, 8.51, 2.06, 0.4, 0.11),
('감자국', '그릇', 500, 'NORMAL_SOUP_AND_TANG', FALSE, 21.0, 0.9, 0.26, 3.67, 0.85),
('갈치찌개', '그릇', 500, 'NORMAL_STEW_AND_HOT_POT', FALSE, 38.0, 5.27, 1.0, 1.95, 0.98),
('감자탕', '그릇', 500, 'NORMAL_STEW_AND_HOT_POT', FALSE, 71.0, 6.31, 4.06, 2.27, 0.0),
('고구마_찐고구마', '개', 200, 'NORMAL_STEAMED', FALSE, 139.0, 1.67, 0.24, 32.47, 16.32),
('가오리찜', '그릇', 500, 'NORMAL_STEAMED', FALSE, 102.0, 21.99, 1.39, 0.46, 0.06),
('가자미구이', '마리', 300, 'NORMAL_GRILLED', FALSE, 123.0, 18.7, 5.02, 0.66, 0.0),
('갈비구이_소고기', '개', 200, 'NORMAL_GRILLED', FALSE, 241.0, 25.91, 14.55, 1.59, 0.26),
('가지전', '개', 50, 'NORMAL_PANCAKE', FALSE, 123.0, 5.48, 7.28, 8.88, 0.84),
('감자전', '개', 50, 'NORMAL_PANCAKE', FALSE, 133.0, 1.72, 6.54, 16.79, 1.81),
('감자볶음', '그릇', 300, 'NORMAL_STIR_FRIED', FALSE, 100.0, 1.75, 4.84, 12.41, 3.37),
('감자볶음_채소', '그릇', 300, 'NORMAL_STIR_FRIED', FALSE, 108.0, 2.04, 5.22, 13.1, 0.12),
('감자조림', '그릇', 300, 'NORMAL_BRAISED', FALSE, 114.0, 2.4, 3.52, 18.1, 7.68),
('고등어조림', '마리', 300, 'NORMAL_BRAISED', FALSE, 141.0, 11.25, 8.84, 4.1, 3.39),
('감자튀김', '개', 100, 'NORMAL_FRIED', FALSE, 312.0, 4.55, 17.01, 35.15, 0.37),
('김튀김', '개', 50, 'NORMAL_FRIED', FALSE, 522.0, 11.0, 28.9, 54.36, 0.14),
('가지나물', '그릇', 200, 'NORMAL_NAMUL_AND_SUKCHAE', FALSE, 36.0, 1.42, 1.5, 4.18, 2.34),
('고구마줄기나물', '그릇', 200, 'NORMAL_NAMUL_AND_SUKCHAE', FALSE, 83.0, 0.98, 7.0, 4.1, 0.0),
('가죽나물무침', '그릇', 200, 'NORMAL_FRESH_AND_MUCHIM', FALSE, 76.0, 5.83, 1.35, 10.05, 3.81),
('겉절이_배추', '그릇', 200, 'NORMAL_FRESH_AND_MUCHIM', FALSE, 55.0, 2.28, 2.14, 6.58, 1.3),
('고들빼기김치', '그릇', 200, 'NORMAL_KIMCHI', FALSE, 71.0, 3.0, 1.38, 11.52, 4.4),
('깍두기', '그릇', 100, 'NORMAL_KIMCHI', FALSE, 33.0, 1.56, 1.06, 4.24, 2.54),
('가자미식해', '그릇', 200, 'NORMAL_SALTED_SEAFOOD', FALSE, 134.0, 11.09, 4.81, 11.64, 3.71),
('갈치젓_양념', '그릇', 200, 'NORMAL_SALTED_SEAFOOD', FALSE, 149.0, 15.53, 5.08, 10.28, 4.91),
('단무지', '그릇', 100, 'NORMAL_PICKLE', FALSE, 13.0, 0.33, 0.2, 2.4, 0.47),
('더덕장아찌', '그릇', 200, 'NORMAL_PICKLE', FALSE, 141.0, 4.47, 0.89, 28.9, 15.38),
('미숫가루', '그릇', 200, 'NORMAL_BEVERAGE_AND_TEA', FALSE, 70.0, 2.08, 2.27, 10.42, 0.0),
('육회', '그릇', 200, 'NORMAL_FISH_AND_MEAT', FALSE, 130.0, 9.93, 6.43, 8.12, 5.29),
('소스/드레싱_이디야초콜릿디핑소스', '개', 50, 'NORMAL_SAUCE_AND_SEASONING', FALSE, 280.0, 2.5, 5.54, 7.66, 47.5),
('카레소스_치킨마크니 찍먹커리', '개', 100, 'NORMAL_SAUCE_AND_SEASONING', FALSE, 172.0, 3.65, 3.488, 28.01, 10.24),
('빙수_22오리지널 팥빙수', '개', 500, 'NORMAL_DAIRY_AND_ICECREAM', FALSE, 136.0, 2.68, 2.766842105, 25.46733333, 18.35),
('빙수_그린티 초코 쿨빙수', '개', 500, 'NORMAL_DAIRY_AND_ICECREAM', FALSE, 138.0, 1.72, 2.766842105, 25.46733333, 15.5),
('과ㆍ채주스_22수박 주스 (L)', '개', 500, 'NORMAL_BEVERAGE_AND_TEA', FALSE, 48.0, 0.59, 0.2555, 7.905217391, 10.39),
('과일_하루 한 컵 RED', '개', 200, 'NORMAL_FRUIT', FALSE, 43.0, 0.47, 7.240944339, 19.58296014, 7.07),
('견과_넛츠앤푸르츠', '개', 100, 'NORMAL_BEANS_AND_NUTS', FALSE, 550.0, 17.5, 37.5, 37.5, 17.5),
('고구마_밤고구마_삶은것', '개', 200, 'NORMAL_GRAIN_AND_TUBER', FALSE, 132.0, 2.45, 0.12, 30.17, 18.43),
('팝콘', '개', 100, 'NORMAL_GRAIN_AND_TUBER', FALSE, 511.0, 8.3, 27.34, 58.06, 0.38),
('달걀_삶은것', '개', 50, 'NORMAL_FISH_AND_MEAT', FALSE, 150.0, 12.8, 10.65, 0.63, 0.62),
('파강회', '그릇', 200, 'NORMAL_VEGETABLE_AND_SEAWEED', FALSE, 42.0, 1.2, 1.71, 6.33, 3.39),
('33한체다치즈팝콘', '개', 100, 'PROCESSED_BREAD_AND_SNACK', TRUE, 478.0, 9.34, 19.5, 66.2, 4.58),
('CGV시그니처스위트팝콘', '개', 100, 'PROCESSED_BREAD_AND_SNACK', TRUE, 529.0, 4.29, 28.57, 64.29, 22.86),
('6시내고향', '개', 500, 'PROCESSED_ICECREAM', TRUE, 264.0, 3.64, 19.09, 18.18, 13.64),
('REALPrice바닐라아이스크림', '개', 500, 'PROCESSED_ICECREAM', TRUE, 130.0, 2.0, 5.4, 18.0, 16.0),
('뉴얼려먹는초코만들기', '개', 100, 'PROCESSED_COCOA_CHOCOLATE', TRUE, 528.0, 6.67, 27.78, 63.89, 38.89),
('다크 초콜릿', '개', 100, 'PROCESSED_COCOA_CHOCOLATE', TRUE, 598.0, 7.79, 42.63, 45.9, 23.99),
('유기농황설탕', '개', 100, 'PROCESSED_SUGAR', TRUE, 400.0, 0.0, 0.0, 100.0, 100.0),
('백설 담금용 브라운 자일로스 설탕', '개', 100, 'PROCESSED_SUGAR', TRUE, 400.0, 0.06, 0.04, 99.8, 90.33),
('100과일로만든라즈베리잼', '개', 100, 'PROCESSED_JAM', TRUE, 184.0, 1.0, 0.0, 45.0, 44.0),
('100과일로만든레드과일잼', '개', 100, 'PROCESSED_JAM', TRUE, 176.0, 1.0, 0.0, 43.0, 43.0),
('두부', '개', 300, 'PROCESSED_TOFU_AND_MUK', TRUE, 97.0, 9.62, 4.63, 3.75, 0.0),
('1등급 국산콩 두부', '개', 300, 'PROCESSED_TOFU_AND_MUK', TRUE, 80.0, 8.0, 4.2, 3.0, 0.4),
('(급식용)프리미엄식용유', '개', 100, 'PROCESSED_OIL', TRUE, 900.0, 0.0, 100.0, 0.0, 0.0),
('곰표콩식용유', '개', 100, 'PROCESSED_OIL', TRUE, 900.0, 0.0, 100.0, 0.0, 0.0),
('간편한생수제비', '개', 200, 'PROCESSED_NOODLE', TRUE, 276.0, 6.97, 0.79, 60.0, 3.0),
('도삭면', '개', 200, 'PROCESSED_NOODLE', TRUE, 267.0, 7.0, 0.8, 58.0, 3.9),
('두충차', '개', 500, 'PROCESSED_BEVERAGE', TRUE, 0.0, 0.0, 0.0, 0.0, 0.0),
('순녹차', '개', 500, 'PROCESSED_BEVERAGE', TRUE, 0.0, 0.0, 0.0, 0.0, 0.0),
('남양임페리얼드림XOAfterFormula', '개', 100, 'PROCESSED_NUTRITION', TRUE, 443.0, 19.29, 14.29, 60.0, 46.43),
('남양임페리얼드림XOWorldClass액상12개월부터24개월까지', '개', 100, 'PROCESSED_NUTRITION', TRUE, 66.0, 2.3, 2.8, 8.0, 6.2),
('MEDIWELL 메디웰 TF 티에프', '개', 100, 'PROCESSED_MEDICAL', TRUE, 100.0, 4.0, 3.0, 15.0, 1.0),
('MEDIWELL 메디웰 구수한맛', '개', 100, 'PROCESSED_MEDICAL', TRUE, 100.0, 4.0, 3.5, 13.5, 3.5),
('청강 메주', '개', 100, 'PROCESSED_SAUCE', TRUE, 403.0, 33.89, 18.94, 24.39, 0.0),
('초정팥메주가루', '개', 100, 'PROCESSED_SAUCE', TRUE, 453.0, 38.39, 20.74, 28.1, 0.0),
('2배 사과식초', '개', 100, 'PROCESSED_SEASONING', TRUE, 12.0, 0.0, 0.0, 3.0, 3.0),
('CJ 하선정 포기김치', '개', 500, 'PROCESSED_PICKLE_AND_BRAISED', TRUE, 30.0, 2.0, 0.0, 5.0, 2.0),
('건강한 태양초 포기김치', '개', 500, 'PROCESSED_PICKLE_AND_BRAISED', TRUE, 35.0, 2.0, 0.6, 6.0, 4.0),
('막걸리(알코올 6)', '개', 500, 'PROCESSED_ALCOHOL', TRUE, 54.0, 0.98, 0.15, 1.56, 0.52),
('쌀은원래달다 9°', '개', 500, 'PROCESSED_ALCOHOL', TRUE, 122.0, 0.0, 0.0, 0.0, 0.0),
('강력밀가루', '개', 100, 'PROCESSED_AGRICULTURAL', TRUE, 355.0, 13.0, 1.7, 72.0, 2.37),
('강력밀가루(제빵전용분)', '개', 100, 'PROCESSED_AGRICULTURAL', TRUE, 355.0, 13.0, 1.3, 73.0, 0.0),
('LM-훈제닭다리', '개', 200, 'PROCESSED_MEAT', TRUE, 215.0, 20.0, 14.0, 2.0, 2.0),
('OKCOOK부드러운닭가슴살', '개', 200, 'PROCESSED_MEAT', TRUE, 131.0, 24.62, 2.46, 2.31, 0.54),
('가염전란액', '개', 100, 'PROCESSED_EGG', TRUE, 155.0, 12.6, 10.6, 1.12, 0.0),
('동물복지 계란으로 만든 액상 계란', '개', 100, 'PROCESSED_EGG', TRUE, 128.0, 12.0, 8.0, 4.0, 0.0),
('1단계분유', '개', 100, 'PROCESSED_DAIRY', TRUE, 508.0, 12.41, 25.75, 56.88, 49.01),
('2단계분유', '개', 100, 'PROCESSED_DAIRY', TRUE, 506.0, 12.63, 25.38, 56.93, 49.0),
('당근배합육', '개', 200, 'PROCESSED_SEAFOOD', TRUE, 84.0, 6.0, 0.3, 14.0, 2.0),
('당근배합육(냉동)', '개', 200, 'PROCESSED_SEAFOOD', TRUE, 84.0, 6.0, 0.3, 14.0, 2.0),
('식용건조밀웜', '개', 100, 'PROCESSED_ANIMAL', TRUE, 463.0, 51.99, 18.61, 21.83, 0.96),
('it`s gum 이츠굼 생기가득', '개', 10, 'PROCESSED_ANIMAL', TRUE, 6.0, 0.58, 0.01, 0.87, 0.75),
('1타덮밥', '개', 300, 'PROCESSED_INSTANT', TRUE, 201.0, 6.15, 7.38, 27.38, 4.31),
('2XL매운치즈볶음밥', '개', 300, 'PROCESSED_INSTANT', TRUE, 205.0, 6.0, 5.33, 33.33, 2.67),
('TS맥주효모환', '개', 100, 'PROCESSED_OTHER_FOOD', TRUE, 366.0, 43.99, 4.1, 38.39, 0.0),
('구트하르 프리미엄 독일 100 맥주효모환', '개', 100, 'PROCESSED_OTHER_FOOD', TRUE, 360.0, 48.1, 0.1, 41.7, 0.4);

INSERT INTO `aifood` (`user_id`, `name`, `unit_type`, `unit_num`, `food_type`, `is_processed_food`, `calorie`, `carbohydrate`, `protein`, `fat`, `sugar`) VALUES
(1, 'AI_삼겹살_구이', '개', 200, 'NORMAL_GRILLED', FALSE, 250.0, 0.0, 20.0, 18.0, 0.0),
(1, 'AI_된장찌개', '그릇', 500, 'NORMAL_STEW_AND_HOT_POT', FALSE, 120.0, 8.0, 12.0, 5.0, 2.0),
(2, 'AI_김치볶음밥', '그릇', 400, 'NORMAL_STIR_FRIED', FALSE, 280.0, 45.0, 8.0, 8.0, 3.0),
(1, 'AI_계란말이', '개', 100, 'NORMAL_PANCAKE', FALSE, 180.0, 2.0, 15.0, 12.0, 1.0),
(2, 'AI_콩나물무침', '그릇', 200, 'NORMAL_NAMUL_AND_SUKCHAE', FALSE, 60.0, 8.0, 4.0, 2.0, 1.0);

INSERT INTO `meal_food` (`meal_id`, `food_id`, `quantity`) VALUES
-- record - 아침(1), 점심(2)
(1, 1, 1.0),   -- 아침: 국밥_돼지머리 1그릇
(1, 15, 0.5),  -- 아침: 가자미구이 0.5마리
(2, 2, 1.0),   -- 점심: 국밥_순대국밥 1그릇
(2, 18, 2.0),  -- 점심: 감자전 2개

-- plan - 저녁(3)
(3, 5, 1.0),   -- 저녁: 간자장 1그릇
(3, 20, 1.0),  -- 저녁: 감자조림 1그릇

-- ai_plan - AI 계획들(4,5,6,7,8)
(4, 3, 1.0),   -- AI 아침: 가래떡 1개
(4, 25, 1.0),  -- AI 아침: 가지나물 1그릇
(5, 6, 1.0),   -- AI 점심: 국수_막국수 1그릇
(5, 22, 1.0),  -- AI 점심: 고등어조림 1마리
(6, 13, 1.0),  -- AI 아침: 고구마_찐고구마 1개
(6, 28, 1.0),  -- AI 아침: 고들빼기김치 1그릇
(7, 16, 1.0),  -- AI 아침: 갈비구이_소고기 1개
(7, 30, 1.0),  -- AI 아침: 가자미식해 1그릇
(8, 4, 2.0),   -- AI 아침: 경단_깨 2개
(8, 32, 1.0);  -- AI 아침: 단무지 1그릇

INSERT INTO `meal_aifood` (`meal_id`, `aifood_id`, `quantity`) VALUES
-- record - 점심(1)에 AI 음식 추가
(1, 1, 1.0),   -- 점심: AI_삼겹살_구이 1개
(1, 2, 1.0),   -- 점심: AI_된장찌개 1그릇

-- plan - 저녁(3)에 AI 음식 추가
(3, 2, 1.0),   -- 저녁: AI_된장찌개 1그릇

-- ai_plan - AI 계획들에 AI 음식 추가
(4, 3, 1.0),   -- AI 아침: AI_김치볶음밥 1그릇
(5, 4, 1.0),   -- AI 점심: AI_계란말이 1개
(6, 5, 1.0),   -- AI 아침: AI_콩나물무침 1그릇
(7, 1, 0.5),   -- AI 아침: AI_삼겹살_구이 0.5개
(8, 2, 1.0);   -- AI 아침: AI_된장찌개 1그릇


-- 운동 관련 테이블 (수정 필요)
INSERT INTO `exercise` (`name`, `target_muscle_group`, `met_value`) VALUES
('벤치프레스', '가슴', 5.0),
('스쿼트', '하체', 8.0),
('달리기', '유산소', 7.0);

INSERT INTO `set_detail` (`count`, `weight_kg`, `weight_num`, `distance`, `time`, `order`) VALUES
(10, 60, 0, 0, 0.0, 1),
(12, 50, 0, 0, 0.0, 2),
(0, 0, 0, 5000, 30.0, 1);

INSERT INTO `exercise_record` (`user_id`, `exercise_date`) VALUES
(1, CURDATE());

INSERT INTO `exercise_detail` (`exercise_record_id`, `exercise_id`, `set_detail_id`, `user_id`, `time`, `intensity`) VALUES
(1, 1, 1, 1, 60, 'NORMAL'),
(1, 2, 2, 1, 90, 'TIGHT');

INSERT INTO `exercise_routine` (`user_id`, `name`) VALUES
(1, '주 3회 근력 루틴');

INSERT INTO `exercise_routine_information` (`exercise_routine_id`, `exercise_id`) VALUES
(1, 1),
(1, 2);
