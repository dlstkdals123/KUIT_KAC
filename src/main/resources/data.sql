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
INSERT INTO `diet` (`user_id`, `name`, `diet_type`, `diet_entry_type`, `diet_time`) VALUES
(1, '오늘의 아침 기록', 'BREAKFAST', 'RECORD', CONCAT(CURDATE(), ' 09:00:00')),
(1, '오늘의 점심 기록', 'LUNCH', 'RECORD', CONCAT(CURDATE(), ' 13:00:00')),
(1, '나만의 아침', 'TEMPLATE', NULL, CURDATE()),
(1, '나만의 점심', 'TEMPLATE', NULL, CURDATE()),
(1, '나만의 저녁', 'TEMPLATE', NULL, CURDATE()),
(2, '아침 계획', 'BREAKFAST', 'PLAN', NULL),
(2, '점심 계획', 'LUNCH', 'PLAN', NULL),
(2, '저녁 계획', 'DINNER', 'PLAN', NULL);

INSERT INTO `food` (`name`, `unit_type`, `unit_num`, `food_type`, `is_processed_food`, `calorie`, `carbohydrate`, `is_high_carbonhydrate`, `protein`, `is_high_protein`, `fat`, `is_high_fat`, `sugar`, `score`) VALUES
('국밥_돼지머리', '그릇', 500, 'NORMAL_RICE', FALSE, 137.0, 6.7, FALSE, 5.16, FALSE, 15.94, FALSE, 0.16, 75),
('국밥_순대국밥', '그릇', 500, 'NORMAL_RICE', FALSE, 75.0, 3.17, FALSE, 2.28, FALSE, 10.38, FALSE, 0.17, 65),
('가래떡', '개', 100, 'NORMAL_BREAD_AND_SNACK', FALSE, 195.0, 3.92, FALSE, 0.51, FALSE, 43.73, TRUE, 0.0, 45),
('경단_깨', '개', 50, 'NORMAL_BREAD_AND_SNACK', FALSE, 240.0, 6.28, FALSE, 6.72, FALSE, 38.58, TRUE, 0.61, 50),
('간자장', '그릇', 500, 'NORMAL_NOODLE_AND_DUMPLING', FALSE, 125.0, 5.12, FALSE, 4.89, FALSE, 15.13, FALSE, 0.86, 70),
('국수_막국수', '그릇', 500, 'NORMAL_NOODLE_AND_DUMPLING', FALSE, 133.0, 5.8, FALSE, 1.61, FALSE, 23.9, TRUE, 4.88, 60),
('닭죽', '그릇', 300, 'NORMAL_PORRIDGE_AND_SOUP', FALSE, 44.0, 2.68, FALSE, 0.57, FALSE, 7.14, FALSE, 0.0, 80),
('스프_양송이버섯', '그릇', 300, 'NORMAL_PORRIDGE_AND_SOUP', FALSE, 105.0, 2.41, FALSE, 7.04, TRUE, 8.06, FALSE, 2.52, 85),
('갈비탕', '그릇', 500, 'NORMAL_SOUP_AND_TANG', FALSE, 54.0, 8.51, FALSE, 2.06, FALSE, 0.4, FALSE, 0.11, 75),
('감자국', '그릇', 500, 'NORMAL_SOUP_AND_TANG', FALSE, 21.0, 0.9, FALSE, 0.26, FALSE, 3.67, FALSE, 0.85, 70),
('갈치찌개', '그릇', 500, 'NORMAL_STEW_AND_HOT_POT', FALSE, 38.0, 5.27, FALSE, 1.0, FALSE, 1.95, FALSE, 0.98, 75),
('감자탕', '그릇', 500, 'NORMAL_STEW_AND_HOT_POT', FALSE, 71.0, 6.31, FALSE, 4.06, FALSE, 2.27, FALSE, 0.0, 80),
('고구마_찐고구마', '개', 200, 'NORMAL_STEAMED', FALSE, 139.0, 1.67, FALSE, 0.24, FALSE, 32.47, TRUE, 16.32, 60),
('가오리찜', '그릇', 500, 'NORMAL_STEAMED', FALSE, 102.0, 21.99, TRUE, 1.39, FALSE, 0.46, FALSE, 0.06, 70),
('가자미구이', '마리', 300, 'NORMAL_GRILLED', FALSE, 123.0, 18.7, TRUE, 5.02, FALSE, 0.66, FALSE, 0.0, 75),
('갈비구이_소고기', '개', 200, 'NORMAL_GRILLED', FALSE, 241.0, 25.91, TRUE, 14.55, TRUE, 1.59, FALSE, 0.26, 85),
('가지전', '개', 50, 'NORMAL_PANCAKE', FALSE, 123.0, 5.48, FALSE, 7.28, TRUE, 8.88, FALSE, 0.84, 70),
('감자전', '개', 50, 'NORMAL_PANCAKE', FALSE, 133.0, 1.72, FALSE, 6.54, TRUE, 16.79, TRUE, 1.81, 65),
('감자볶음', '그릇', 300, 'NORMAL_STIR_FRIED', FALSE, 100.0, 1.75, FALSE, 4.84, FALSE, 12.41, FALSE, 3.37, 75),
('감자볶음_채소', '그릇', 300, 'NORMAL_STIR_FRIED', FALSE, 108.0, 2.04, FALSE, 5.22, FALSE, 13.1, FALSE, 0.12, 80),
('감자조림', '그릇', 300, 'NORMAL_BRAISED', FALSE, 114.0, 2.4, FALSE, 3.52, FALSE, 18.1, TRUE, 7.68, 65),
('고등어조림', '마리', 300, 'NORMAL_BRAISED', FALSE, 141.0, 11.25, FALSE, 8.84, TRUE, 4.1, FALSE, 3.39, 80),
('감자튀김', '개', 100, 'NORMAL_FRIED', FALSE, 312.0, 4.55, FALSE, 17.01, TRUE, 35.15, TRUE, 0.37, 45),
('김튀김', '개', 50, 'NORMAL_FRIED', FALSE, 522.0, 11.0, FALSE, 28.9, TRUE, 54.36, TRUE, 0.14, 40),
('가지나물', '그릇', 200, 'NORMAL_NAMUL_AND_SUKCHAE', FALSE, 36.0, 1.42, FALSE, 1.5, FALSE, 4.18, FALSE, 2.34, 85),
('고구마줄기나물', '그릇', 200, 'NORMAL_NAMUL_AND_SUKCHAE', FALSE, 83.0, 0.98, FALSE, 7.0, TRUE, 4.1, FALSE, 0.0, 90),
('가죽나물무침', '그릇', 200, 'NORMAL_FRESH_AND_MUCHIM', FALSE, 76.0, 5.83, FALSE, 1.35, FALSE, 10.05, FALSE, 3.81, 75),
('겉절이_배추', '그릇', 200, 'NORMAL_FRESH_AND_MUCHIM', FALSE, 55.0, 2.28, FALSE, 2.14, FALSE, 6.58, FALSE, 1.3, 80),
('고들빼기김치', '그릇', 200, 'NORMAL_KIMCHI', FALSE, 71.0, 3.0, FALSE, 1.38, FALSE, 11.52, FALSE, 4.4, 70),
('깍두기', '그릇', 100, 'NORMAL_KIMCHI', FALSE, 33.0, 1.56, FALSE, 1.06, FALSE, 4.24, FALSE, 2.54, 75),
('가자미식해', '그릇', 200, 'NORMAL_SALTED_SEAFOOD', FALSE, 134.0, 11.09, FALSE, 4.81, FALSE, 11.64, FALSE, 3.71, 65),
('갈치젓_양념', '그릇', 200, 'NORMAL_SALTED_SEAFOOD', FALSE, 149.0, 15.53, TRUE, 5.08, FALSE, 10.28, FALSE, 4.91, 60),
('단무지', '그릇', 100, 'NORMAL_PICKLE', FALSE, 13.0, 0.33, FALSE, 0.2, FALSE, 2.4, FALSE, 0.47, 85),
('더덕장아찌', '그릇', 200, 'NORMAL_PICKLE', FALSE, 141.0, 4.47, FALSE, 0.89, FALSE, 28.9, TRUE, 15.38, 50),
('미숫가루', '그릇', 200, 'NORMAL_BEVERAGE_AND_TEA', FALSE, 70.0, 2.08, FALSE, 2.27, FALSE, 10.42, FALSE, 0.0, 75),
('육회', '그릇', 200, 'NORMAL_FISH_AND_MEAT', FALSE, 130.0, 9.93, FALSE, 6.43, TRUE, 8.12, FALSE, 5.29, 75),
('소스/드레싱_이디야초콜릿디핑소스', '개', 50, 'NORMAL_SAUCE_AND_SEASONING', FALSE, 280.0, 2.5, FALSE, 5.54, FALSE, 7.66, FALSE, 47.5, 40),
('카레소스_치킨마크니 찍먹커리', '개', 100, 'NORMAL_SAUCE_AND_SEASONING', FALSE, 172.0, 3.65, FALSE, 3.488, FALSE, 28.01, TRUE, 10.24, 45),
('빙수_22오리지널 팥빙수', '개', 500, 'NORMAL_DAIRY_AND_ICECREAM', FALSE, 136.0, 2.68, FALSE, 2.766842105, FALSE, 25.46733333, TRUE, 18.35, 35),
('빙수_그린티 초코 쿨빙수', '개', 500, 'NORMAL_DAIRY_AND_ICECREAM', FALSE, 138.0, 1.72, FALSE, 2.766842105, FALSE, 25.46733333, TRUE, 15.5, 40),
('과ㆍ채주스_22수박 주스 (L)', '개', 500, 'NORMAL_BEVERAGE_AND_TEA', FALSE, 48.0, 0.59, FALSE, 0.2555, FALSE, 7.905217391, FALSE, 10.39, 70),
('과일_하루 한 컵 RED', '개', 200, 'NORMAL_FRUIT', FALSE, 43.0, 0.47, FALSE, 7.240944339, TRUE, 19.58296014, TRUE, 7.07, 85),
('견과_넛츠앤푸르츠', '개', 100, 'NORMAL_BEANS_AND_NUTS', FALSE, 550.0, 17.5, FALSE, 37.5, TRUE, 37.5, TRUE, 17.5, 90),
('고구마_밤고구마_삶은것', '개', 200, 'NORMAL_GRAIN_AND_TUBER', FALSE, 132.0, 2.45, FALSE, 0.12, FALSE, 30.17, TRUE, 18.43, 75),
('팝콘', '개', 100, 'NORMAL_GRAIN_AND_TUBER', FALSE, 511.0, 8.3, FALSE, 27.34, TRUE, 58.06, TRUE, 0.38, 45),
('달걀_삶은것', '개', 50, 'NORMAL_FISH_AND_MEAT', FALSE, 150.0, 12.8, TRUE, 10.65, TRUE, 0.63, FALSE, 0.62, 85),
('파강회', '그릇', 200, 'NORMAL_VEGETABLE_AND_SEAWEED', FALSE, 42.0, 1.2, FALSE, 1.71, FALSE, 6.33, FALSE, 3.39, 80);

-- 가공식품 데이터들
INSERT INTO `food` (`name`, `unit_type`, `unit_num`, `food_type`, `is_processed_food`, `calorie`, `carbohydrate`, `is_high_carbonhydrate`, `protein`, `is_high_protein`, `fat`, `is_high_fat`, `sugar`, `score`) VALUES
('33한체다치즈팝콘', '개', 100, 'PROCESSED_BREAD_AND_SNACK', TRUE, 478.0, 9.34, FALSE, 19.5, TRUE, 66.2, TRUE, 4.58, 35),
('CGV시그니처스위트팝콘', '개', 100, 'PROCESSED_BREAD_AND_SNACK', TRUE, 529.0, 4.29, FALSE, 28.57, TRUE, 64.29, TRUE, 22.86, 30),
('6시내고향', '개', 500, 'PROCESSED_ICECREAM', TRUE, 264.0, 3.64, FALSE, 19.09, TRUE, 18.18, FALSE, 13.64, 40),
('REALPrice바닐라아이스크림', '개', 500, 'PROCESSED_ICECREAM', TRUE, 130.0, 2.0, FALSE, 5.4, FALSE, 18.0, FALSE, 16.0, 45),
('뉴얼려먹는초코만들기', '개', 100, 'PROCESSED_COCOA_CHOCOLATE', TRUE, 528.0, 6.67, FALSE, 27.78, TRUE, 63.89, TRUE, 38.89, 25),
('다크 초콜릿', '개', 100, 'PROCESSED_COCOA_CHOCOLATE', TRUE, 598.0, 7.79, FALSE, 42.63, TRUE, 45.9, TRUE, 23.99, 30),
('유기농황설탕', '개', 100, 'PROCESSED_SUGAR', TRUE, 400.0, 0.0, FALSE, 0.0, FALSE, 100.0, TRUE, 100.0, 10),
('백설 담금용 브라운 자일로스 설탕', '개', 100, 'PROCESSED_SUGAR', TRUE, 400.0, 0.06, FALSE, 0.04, FALSE, 99.8, TRUE, 90.33, 15),
('100과일로만든라즈베리잼', '개', 100, 'PROCESSED_JAM', TRUE, 184.0, 1.0, FALSE, 0.0, FALSE, 45.0, TRUE, 44.0, 35),
('100과일로만든레드과일잼', '개', 100, 'PROCESSED_JAM', TRUE, 176.0, 1.0, FALSE, 0.0, FALSE, 43.0, TRUE, 43.0, 40),
('두부', '개', 300, 'PROCESSED_TOFU_AND_MUK', TRUE, 97.0, 9.62, FALSE, 4.63, FALSE, 3.75, FALSE, 0.0, 85),
('1등급 국산콩 두부', '개', 300, 'PROCESSED_TOFU_AND_MUK', TRUE, 80.0, 8.0, FALSE, 4.2, FALSE, 3.0, FALSE, 0.4, 90),
('(급식용)프리미엄식용유', '개', 100, 'PROCESSED_OIL', TRUE, 900.0, 0.0, FALSE, 0.0, FALSE, 100.0, TRUE, 0.0, 20),
('곰표콩식용유', '개', 100, 'PROCESSED_OIL', TRUE, 900.0, 0.0, FALSE, 0.0, FALSE, 100.0, TRUE, 0.0, 25),
('간편한생수제비', '개', 200, 'PROCESSED_NOODLE', TRUE, 276.0, 6.97, FALSE, 0.79, FALSE, 60.0, TRUE, 3.0, 45),
('도삭면', '개', 200, 'PROCESSED_NOODLE', TRUE, 267.0, 7.0, FALSE, 0.8, FALSE, 58.0, TRUE, 3.9, 50),
('두충차', '개', 500, 'PROCESSED_BEVERAGE', TRUE, 0.0, 0.0, FALSE, 0.0, FALSE, 0.0, FALSE, 0.0, 95),
('순녹차', '개', 500, 'PROCESSED_BEVERAGE', TRUE, 0.0, 0.0, FALSE, 0.0, FALSE, 0.0, FALSE, 0.0, 100),
('남양임페리얼드림XOAfterFormula', '개', 100, 'PROCESSED_NUTRITION', TRUE, 443.0, 19.29, FALSE, 14.29, TRUE, 60.0, TRUE, 46.43, 70),
('남양임페리얼드림XOWorldClass액상12개월부터24개월까지', '개', 100, 'PROCESSED_NUTRITION', TRUE, 66.0, 2.3, FALSE, 2.8, FALSE, 8.0, FALSE, 6.2, 85),
('MEDIWELL 메디웰 TF 티에프', '개', 100, 'PROCESSED_MEDICAL', TRUE, 100.0, 4.0, FALSE, 3.0, FALSE, 15.0, FALSE, 1.0, 90),
('MEDIWELL 메디웰 구수한맛', '개', 100, 'PROCESSED_MEDICAL', TRUE, 100.0, 4.0, FALSE, 3.5, FALSE, 13.5, FALSE, 3.5, 85),
('청강 메주', '개', 100, 'PROCESSED_SAUCE', TRUE, 403.0, 33.89, TRUE, 18.94, TRUE, 24.39, TRUE, 0.0, 60),
('초정팥메주가루', '개', 100, 'PROCESSED_SAUCE', TRUE, 453.0, 38.39, TRUE, 20.74, TRUE, 28.1, TRUE, 0.0, 55),
('2배 사과식초', '개', 100, 'PROCESSED_SEASONING', TRUE, 12.0, 0.0, FALSE, 0.0, FALSE, 3.0, FALSE, 3.0, 85),
('CJ 하선정 포기김치', '개', 500, 'PROCESSED_PICKLE_AND_BRAISED', TRUE, 30.0, 2.0, FALSE, 0.0, FALSE, 5.0, FALSE, 2.0, 75),
('건강한 태양초 포기김치', '개', 500, 'PROCESSED_PICKLE_AND_BRAISED', TRUE, 35.0, 2.0, FALSE, 0.6, FALSE, 6.0, FALSE, 4.0, 80),
('막걸리(알코올 6)', '개', 500, 'PROCESSED_ALCOHOL', TRUE, 54.0, 0.98, FALSE, 0.15, FALSE, 1.56, FALSE, 0.52, 30),
('쌀은원래달다 9°', '개', 500, 'PROCESSED_ALCOHOL', TRUE, 122.0, 0.0, FALSE, 0.0, FALSE, 0.0, FALSE, 0.0, 25),
('강력밀가루', '개', 100, 'PROCESSED_AGRICULTURAL', TRUE, 355.0, 13.0, FALSE, 1.7, FALSE, 72.0, TRUE, 2.37, 45),
('강력밀가루(제빵전용분)', '개', 100, 'PROCESSED_AGRICULTURAL', TRUE, 355.0, 13.0, FALSE, 1.3, FALSE, 73.0, TRUE, 0.0, 40),
('LM-훈제닭다리', '개', 200, 'PROCESSED_MEAT', TRUE, 215.0, 20.0, FALSE, 14.0, TRUE, 2.0, FALSE, 2.0, 70),
('OKCOOK부드러운닭가슴살', '개', 200, 'PROCESSED_MEAT', TRUE, 131.0, 24.62, TRUE, 2.46, FALSE, 2.31, FALSE, 0.54, 75),
('가염전란액', '개', 100, 'PROCESSED_EGG', TRUE, 155.0, 12.6, FALSE, 10.6, TRUE, 1.12, FALSE, 0.0, 80),
('동물복지 계란으로 만든 액상 계란', '개', 100, 'PROCESSED_EGG', TRUE, 128.0, 12.0, FALSE, 8.0, TRUE, 4.0, FALSE, 0.0, 85),
('1단계분유', '개', 100, 'PROCESSED_DAIRY', TRUE, 508.0, 12.41, FALSE, 25.75, TRUE, 56.88, TRUE, 49.01, 75),
('2단계분유', '개', 100, 'PROCESSED_DAIRY', TRUE, 506.0, 12.63, FALSE, 25.38, TRUE, 56.93, TRUE, 49.0, 70),
('당근배합육', '개', 200, 'PROCESSED_SEAFOOD', TRUE, 84.0, 6.0, FALSE, 0.3, FALSE, 14.0, FALSE, 2.0, 75),
('당근배합육(냉동)', '개', 200, 'PROCESSED_SEAFOOD', TRUE, 84.0, 6.0, FALSE, 0.3, FALSE, 14.0, FALSE, 2.0, 75),
('식용건조밀웜', '개', 100, 'PROCESSED_ANIMAL', TRUE, 463.0, 51.99, TRUE, 18.61, TRUE, 21.83, TRUE, 0.96, 60),
('it`s gum 이츠굼 생기가득', '개', 10, 'PROCESSED_ANIMAL', TRUE, 6.0, 0.58, FALSE, 0.01, FALSE, 0.87, FALSE, 0.75, 20),
('1타덮밥', '개', 300, 'PROCESSED_INSTANT', TRUE, 201.0, 6.15, FALSE, 7.38, TRUE, 27.38, TRUE, 4.31, 55),
('2XL매운치즈볶음밥', '개', 300, 'PROCESSED_INSTANT', TRUE, 205.0, 6.0, FALSE, 5.33, FALSE, 33.33, TRUE, 2.67, 50),
('TS맥주효모환', '개', 100, 'PROCESSED_OTHER_FOOD', TRUE, 366.0, 43.99, TRUE, 4.1, FALSE, 38.39, TRUE, 0.0, 45),
('구트하르 프리미엄 독일 100 맥주효모환', '개', 100, 'PROCESSED_OTHER_FOOD', TRUE, 360.0, 48.1, TRUE, 0.1, FALSE, 41.7, TRUE, 0.4, 40);

INSERT INTO `aifood` (`user_id`, `name`, `unit_type`, `unit_num`, `food_type`, `is_processed_food`, `calorie`, `carbohydrate`, `is_high_carbonhydrate`, `protein`, `is_high_protein`, `fat`, `is_high_fat`, `sugar`, `score`) VALUES
(1, 'AI_삼겹살_구이', '개', 200, 'NORMAL_GRILLED', FALSE, 250.0, 0.0, FALSE, 20.0, TRUE, 18.0, FALSE, 0.0, 75),
(1, 'AI_된장찌개', '그릇', 500, 'NORMAL_STEW_AND_HOT_POT', FALSE, 120.0, 8.0, FALSE, 12.0, TRUE, 5.0, FALSE, 2.0, 85),
(2, 'AI_김치볶음밥', '그릇', 400, 'NORMAL_STIR_FRIED', FALSE, 280.0, 45.0, TRUE, 8.0, TRUE, 8.0, FALSE, 3.0, 70),
(1, 'AI_계란말이', '개', 100, 'NORMAL_PANCAKE', FALSE, 180.0, 2.0, FALSE, 15.0, TRUE, 12.0, FALSE, 1.0, 80),
(2, 'AI_콩나물무침', '그릇', 200, 'NORMAL_NAMUL_AND_SUKCHAE', FALSE, 60.0, 8.0, FALSE, 4.0, FALSE, 2.0, FALSE, 1.0, 90);

INSERT INTO `diet_food` (`diet_id`, `food_id`, `quantity`) VALUES
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

INSERT INTO `diet_aifood` (`diet_id`, `aifood_id`, `quantity`) VALUES
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