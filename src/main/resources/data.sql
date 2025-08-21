# -- 외래키 제약 조건 잠깐 꺼두기
# SET FOREIGN_KEY_CHECKS = 0;
#
# TRUNCATE TABLE routine_set;
# TRUNCATE TABLE routine_detail;
# TRUNCATE TABLE routine_exercise;
# TRUNCATE TABLE routine;
# TRUNCATE TABLE exercise;
#
# TRUNCATE TABLE diet_food;
# TRUNCATE TABLE food;
# TRUNCATE TABLE diet;
#
# TRUNCATE TABLE weight;
# TRUNCATE TABLE user_term_agreement;
# TRUNCATE TABLE user_information;
# TRUNCATE TABLE user;
#
# SET FOREIGN_KEY_CHECKS = 1;
#

-- 사용자 관련 테이블
INSERT IGNORE INTO user (`nickname`, `kakao_id`, `gender`, `age`, `height`, `target_weight`)
VALUES ('user4', '0000000001', 'MALE', 30, 175, 70.5),
       ('user5', '0000000002', 'FEMALE', 25, 163, 55.0),
       ('admin@test', '9999999999', 'MALE', 29, 180, 70.0);

SET @admin_id = LAST_INSERT_ID();

INSERT ignore INTO user_information
(`user_id`, `has_diet_experience`, `diet_fail_reason`, `appetite_type`,
 `weekly_eating_out_count`, `eating_out_type`, `diet_velocity`, `activity`)
VALUES (1, FALSE, NULL, 'SMALL', '4번 이상', 'KOREAN', 'COACH', 'VERY_HIGH'),
       (2, TRUE, '야식', 'BIG', '1번 이하', 'FASTFOOD', 'ALL_IN', 'LOW'),
       (@admin_id, TRUE, NULL, 'BIG', '0', 'KOREAN', 'ALL_IN', 'NORMAL');


INSERT ignore INTO user_term_agreement (user_id, code, version, agreed, agreed_at)
VALUES (1, 'SERVICE_TOS', 'v1.0.0', TRUE, NOW()),
       (1, 'PRIVACY', 'v1.0.0', TRUE, NOW()),
       (1, 'MARKETING', 'v1.0.0', FALSE, NULL),
       (2, 'SERVICE_TOS', 'v1.0.0', TRUE, NOW()),
       (2, 'PRIVACY', 'v1.0.0', TRUE, NOW()),
       (2, 'MARKETING', 'v1.0.0', TRUE, NOW()),
       (@admin_id, 'SERVICE_TOS', 'v1.0.0', TRUE, NOW()),
       (@admin_id, 'PRIVACY', 'v1.0.0', TRUE, NOW()),
       (@admin_id, 'MARKETING', 'v1.0.0', FALSE, NULL);

INSERT INTO `weight` (`user_id`, `weight`)
VALUES (1, 85.0),
       (2, 60.7);


-- 음식 관련 테이블
INSERT INTO `diet` (`user_id`, `name`, `diet_date`, `diet_type`, `diet_entry_type`) VALUES
(1, '오늘의 아침 기록', NULL, 'BREAKFAST', 'RECORD'),
(1, '오늘의 점심 기록', NULL, 'LUNCH', 'RECORD'),
(1, '오늘의 저녁 기록', NULL, 'DINNER', 'RECORD'),
(1, '나만의 아침', NULL, 'TEMPLATE', NULL),
(1, '나만의 점심', NULL, 'TEMPLATE', NULL),
(1, '나만의 저녁', NULL, 'TEMPLATE', NULL),
(2, NULL, CURRENT_DATE(), 'BREAKFAST', 'PLAN'),
(2, NULL, CURRENT_DATE(), 'LUNCH', 'PLAN'),
(2, NULL, CURRENT_DATE(), 'DINNER', 'PLAN'),
(2, NULL, CURRENT_DATE(), 'BREAKFAST', 'FASTING');

-- diet_food 샘플 데이터
INSERT INTO `diet_food` (`diet_id`, `food_id`, `quantity`, `diet_time`)
VALUES (1, 1, 1.7, CONCAT(CURDATE(), ' 08:00:00')),
       (1, 3, 2.3, CONCAT(CURDATE(), ' 08:00:00')),
       (2, 5, 1.2, CONCAT(CURDATE(), ' 12:00:00')),
       (2, 7, 0.8, CONCAT(CURDATE(), ' 12:00:00')),
       (3, 9, 2.1, CONCAT(CURDATE(), ' 18:00:00')),
       (3, 15, 1.4, CONCAT(CURDATE(), ' 18:00:00')),
       (4, 13, 0.9, NULL),
       (4, 17, 2.2, NULL),
       (5, 21, 1.6, NULL),
       (5, 23, 0.7, NULL),
       (6, 25, 2.0, NULL),
       (6, 27, 1.1, NULL),
       (7, 29, 1.3, CONCAT(CURDATE(), ' 03:00:00')),
       (7, 31, 2.4, CONCAT(CURDATE(), ' 03:00:00')),
       (8, 33, 0.6, CONCAT(CURDATE(), ' 03:00:00')),
       (8, 35, 1.8, CONCAT(CURDATE(), ' 03:00:00')),
       (9, 37, 2.5, CONCAT(CURDATE(), ' 03:00:00')),
       (9, 39, 0.5, CONCAT(CURDATE(), ' 03:00:00'));

-- 운동 관련 샘플 데이터
INSERT INTO `routine` (`user_id`, `name`, `routine_type`)
VALUES (1, '상체 기록', 'RECORD'),
       (1, '하체 기록', 'RECORD'),
       (1, '상체 루틴', 'TEMPLATE'),
       (1, '전신 루틴', 'TEMPLATE'),
       (1, '아침 운동 기록', 'RECORD'),
       (2, '전신 기록', 'RECORD'),
       (2, '하체 루틴', 'TEMPLATE'),
       (2, '상체 루틴', 'TEMPLATE'),
       (2, '저녁 운동 기록', 'RECORD'),
       (2, '점심 운동 기록', 'RECORD');

INSERT INTO `routine_exercise` (`routine_id`, `exercise_id`)
VALUES (1, 1),  -- 1번 루틴에 벤치프레스
       (1, 3),  -- 1번 루틴에 데드리프트
       (2, 2),  -- 2번 루틴에 스쿼트
       (3, 5),  -- 3번 루틴에 바이셉 컬
       (4, 6),  -- 4번 루틴에 숄더 프레스
       (5, 7),  -- 5번 루틴에 레그 프레스
       (6, 8),  -- 6번 루틴에 랫풀다운
       (7, 9),  -- 7번 루틴에 레그 익스텐션
       (8, 10), -- 8번 루틴에 레그 컬
       (9, 11), -- 9번 루틴에 카프 레이즈
       (10, 12); -- 10번 루틴에 크런치

INSERT INTO `routine_detail` (`routine_exercise_id`, `time`, `intensity`)
VALUES (1, 30, 'NORMAL'),  -- 벤치프레스 30분
       (2, 20, 'TIGHT'),   -- 데드리프트 20분
       (3, 40, 'LOOSE'),   -- 스쿼트 40분
       (4, 25, 'NORMAL'),  -- 바이셉 컬 25분
       (5, 35, 'TIGHT'),   -- 숄더 프레스 35분
       (6, 45, 'LOOSE'),   -- 레그 프레스 45분
       (7, 30, 'NORMAL'),  -- 랫풀다운 30분
       (8, 25, 'TIGHT'),   -- 레그 익스텐션 25분
       (9, 20, 'LOOSE'),   -- 레그 컬 20분
       (10, 15, 'NORMAL'), -- 카프 레이즈 15분
       (11, 20, 'TIGHT');
-- 크런치 20분

-- 벤치프레스: 1세트(10회), 2세트(8회)
INSERT INTO `routine_set` (`routine_exercise_id`, `count`, `set_order`)
VALUES (1, 10, 1),
       (1, 8, 2),
       (4, 12, 1),
       (4, 10, 2),
       (7, 15, 1),
       (7, 12, 2),
       (10, 20, 1),
       (10, 15, 2);

-- 데드리프트: 1세트(60kg, 8개), 2세트(70kg, 6개)
INSERT INTO `routine_set` (`routine_exercise_id`, `weight_kg`, `weight_num`, `set_order`)
VALUES (2, 60, 8, 1),
       (2, 70, 6, 2),
       (5, 50, 10, 1),
       (5, 55, 8, 2),
       (8, 40, 12, 1),
       (8, 45, 10, 2),
       (11, 30, 15, 1),
       (11, 35, 12, 2);

-- 스쿼트: 1세트(거리 100m), 2세트(거리 120m)
INSERT INTO `routine_set` (`routine_exercise_id`, `distance`, `set_order`)
VALUES (3, 100, 1),
       (3, 120, 2),
       (6, 80, 1),
       (6, 90, 2),
       (9, 150, 1),
       (9, 160, 2);


