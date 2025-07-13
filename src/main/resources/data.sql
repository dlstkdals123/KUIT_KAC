-- 외래 키 제약 조건에 따라 데이터 삭제 (역순)
DELETE FROM `meal_food`;
DELETE FROM `meal`;
DELETE FROM `diet`;
DELETE FROM `exercise_detail`;
DELETE FROM `exercise_record`;
DELETE FROM `weight`;
DELETE FROM `exercise_routine_information`;
DELETE FROM `exercise_routine`;
DELETE FROM `user_information`;
DELETE FROM `set_detail`;
DELETE FROM `exercise`;
DELETE FROM `food`;
DELETE FROM `user`;


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
(1, 'RECORD', CURDATE());

INSERT INTO `meal` (`user_id`, `diet_id`, `name`, `meal_type`, `meal_time`) VALUES
(1, 1, '오늘의 아침', 'BREAKFAST', CONCAT(CURDATE(), ' 09:00:00')),
(1, 1, '오늘의 점심', 'LUNCH', CONCAT(CURDATE(), ' 13:00:00')),
(1, NULL, '나만의 간식', 'TEMPLATE', NULL);

INSERT INTO `food` (`name`, `unit_type`, `unit_num`, `food_type`, `is_processed_food`, `calorie`, `carbohydrate_g`, `protein_g`, `fat_g`, `sugar_g`) VALUES
('샌드위치_닭가슴살', '개', 100, '빵 및 과자류', FALSE, 165.0, 0.0, 31.0, 3.6, 0.0),
('현미밥', '공기', 200, '밥류', FALSE, 280.0, 60.0, 6.0, 2.0, 0.0),
('코카콜라', '캔', 250, '음료류', TRUE, 100.0, 27.0, 0.0, 0.0, 27.0);

INSERT INTO `meal_food` (`meal_id`, `food_id`, `quantity`) VALUES
(1, 1, 0.1),
(2, 2, 1.6),
(3, 3, 1.0);


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
