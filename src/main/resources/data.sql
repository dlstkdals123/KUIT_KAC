INSERT INTO `user` (`nickname`, `password`, `email`, `gender`, `age`, `height`, `target_weight`) VALUES
('user1', 'hashed_password_1', 'user1@example.com', 'male', 30, 175, 70.5),
('user2', 'hashed_password_2', 'user2@example.com', 'female', 25, 163, 55.0);

INSERT INTO `exercise` (`name`, `target_muscle_group`, `met_value`) VALUES
('벤치프레스', '가슴', 5.0),
('스쿼트', '하체', 8.0),
('달리기', '유산소', 7.0);

INSERT INTO `food` (`name`, `unit_type`, `unit_num`, `food_type`, `is_processed_food`, `calorie`, `carbohydrate_g`, `protein_g`, `fat_g`, `sugar_g`) VALUES
('샌드위치_닭가슴살', '개', 100, '빵 및 과자류', FALSE, 165.0, 0.0, 31.0, 3.6, 0.0),
('현미밥', '공기', 200, '밥류', FALSE, 280.0, 60.0, 6.0, 2.0, 0.0),
('코카콜라', '캔', 250, '음료류', TRUE, 100.0, 27.0, 0.0, 0.0, 27.0);

INSERT INTO `diet_template` (`user_id`, `name`, `date`) VALUES
(1, '아침식단1', CURDATE());

INSERT INTO `diet` (`user_id`, `diet_template_id`, `diet_type`, `name`, `diet_date`) VALUES
(1, NULL, 'record', '오늘의 아침', CURDATE()),
(1, 1, 'plan', NULL, CURDATE());

INSERT INTO `meal` (`diet_id`, `meal_type`, `meal_time`) VALUES
(1, 'breakfast', NOW()),
(1, 'lunch', NOW()),
(1, 'snack', NOW());

INSERT INTO `meal_food` (`meal_id`, `food_id`, `quantity`) VALUES
(1, 1, 0.1),
(2, 2, 1.6),
(3, 3, 1.0);

INSERT INTO `set_detail` (`count`, `weight_kg`, `weight_num`, `distance`, `time`, `order`) VALUES
(10, 60, 0, 0, 0.0, 1),
(12, 50, 0, 0, 0.0, 2),
(0, 0, 0, 5000, 30.0, 1);

INSERT INTO `exercise_record` (`user_id`, `exercise_date`) VALUES
(1, CURDATE());

INSERT INTO `exercise_detail` (`exercise_record_id`, `exercise_id`, `set_detail_id`, `user_id`, `time`, `intensity`) VALUES
(1, 1, 1, 1, 60, 'normal'),
(1, 2, 2, 1, 90, 'tight');

INSERT INTO `user_information` (`user_id`, `has_diet_experience`, `diet_fail_reason`, `appetite_type`, `weekly_eating_out_count`, `eating_out_type`, `diet_velocity`) VALUES
(1, TRUE, '야식', 'big', '2-3', 'korean', 'coach');

INSERT INTO `social_dining` (`user_id`, `type`, `social_dining_date`) VALUES
(1, 'dining_out', CURDATE()),
(1, 'drinking', CURDATE());

INSERT INTO `weight` (`user_id`, `weight`) VALUES
(1, 85.0),
(2, 60.7);

INSERT INTO `exercise_routine` (`user_id`, `name`) VALUES
(1, '주 3회 근력 루틴');

INSERT INTO `exercise_routine_information` (`exercise_routine_id`, `exercise_id`) VALUES
(1, 1),
(1, 2);
