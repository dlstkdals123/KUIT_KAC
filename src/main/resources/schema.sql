-- 테이블 삭제 (외래 키 제약 조건 역순)
DROP TABLE IF EXISTS `meal_food`;
DROP TABLE IF EXISTS `meal`;
DROP TABLE IF EXISTS `diet`;
DROP TABLE IF EXISTS `diet_template`;
DROP TABLE IF EXISTS `exercise_detail`;
DROP TABLE IF EXISTS `exercise_record`;
DROP TABLE IF EXISTS `social_dining`;
DROP TABLE IF EXISTS `weight`;
DROP TABLE IF EXISTS `exercise_routine_information`;
DROP TABLE IF EXISTS `exercise_routine`;
DROP TABLE IF EXISTS `user_information`;
DROP TABLE IF EXISTS `set_detail`;
DROP TABLE IF EXISTS `exercise`;
DROP TABLE IF EXISTS `food`;
DROP TABLE IF EXISTS `user`;


-- 사용자 관련 테이블
CREATE TABLE `user` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `nickname`     varchar(20)           NOT NULL,
    `password`     varchar(100)          NOT NULL,
    `email`        varchar(50)           NOT NULL,
    `gender`       ENUM('MALE', 'FEMALE') NOT NULL,
    `age`          int                   NOT NULL,
    `height`       int                   NOT NULL,
    `target_weight` double                NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `user_information` (
    `user_id`                 bigint NOT NULL PRIMARY KEY,
    `has_diet_experience`     boolean NOT NULL DEFAULT FALSE,
    `diet_fail_reason`        varchar(50) NOT NULL,
    `appetite_type`           ENUM('SMALL', 'BIG') NOT NULL,
    `weekly_eating_out_count` varchar(10) NOT NULL,
    `eating_out_type`         ENUM('FASTFOOD', 'KOREAN', 'CHINESE', 'WESTERN', 'FRIED') NOT NULL,
    `diet_velocity`           ENUM('YUMYUM', 'COACH', 'ALL_IN') NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `weight` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`      bigint                NOT NULL,
    `weight`       double                NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);


-- 음식 관련 테이블
CREATE TABLE `diet_template` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`      bigint                NOT NULL,
    `name`         varchar(30)           NOT NULL,
    `date`         date                  NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `diet` (
    `id`               bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`          bigint                NOT NULL,
    `diet_template_id` bigint                NULL,
    `diet_type`        ENUM('RECORD', 'PLAN', 'AI_PLAN', 'PASTING') NULL,
    `name`             varchar(30)           NULL,
    `diet_date`        date                  NULL,
    `created_at`       datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`diet_template_id`) REFERENCES `diet_template`(`id`) ON DELETE SET NULL
);

CREATE TABLE `social_dining` (
    `id`                 bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`            bigint                NOT NULL,
    `type`               ENUM('DINING_OUT', 'DRINKING') NOT NULL,
    `social_dining_date` date                  NULL,
    `created_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `meal` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `diet_id`      bigint                NOT NULL,
    `meal_type`    ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK') NOT NULL,
    `meal_time`    datetime              NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`diet_id`) REFERENCES `diet`(`id`) ON DELETE CASCADE
);

CREATE TABLE `food` (
                        `id`             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
                        `name`           varchar(50)           NOT NULL,
                        `unit_type`      varchar(20)           NOT NULL,
                        `unit_num`       bigint                NOT NULL,
                        `food_type`      varchar(20)           NOT NULL,
                        `is_processed_food` boolean              NOT NULL DEFAULT FALSE,
                        `calorie`        double                NULL,
                        `carbohydrate_g` double                NULL,
                        `protein_g`      double                NULL,
                        `fat_g`          double                NULL,
                        `sugar_g`        double                NULL,
                        `created_at`     datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `updated_at`     datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `meal_food` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `meal_id`      bigint                NOT NULL,
    `food_id`      bigint                NOT NULL,
    `quantity`     double                NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`meal_id`) REFERENCES `meal`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`food_id`) REFERENCES `food`(`id`) ON DELETE CASCADE
);


-- 운동 관련 테이블
CREATE TABLE `exercise` (
    `id`                  bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name`                varchar(40)           NULL,
    `target_muscle_group` varchar(20)           NOT NULL,
    `met_value`           double                NULL,
    `created_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `set_detail` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `count`        int                   NULL DEFAULT 0,
    `weight_kg`    int                   NULL DEFAULT 0,
    `weight_num`   int                   NULL DEFAULT 0,
    `distance`     int                   NULL DEFAULT 0,
    `time`         double                NULL DEFAULT 0,
    `order`        int                   NULL DEFAULT 0,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `exercise_record` (
    `id`            bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`       bigint                NOT NULL,
    `exercise_date` date                  NOT NULL,
    `created_at`    datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `exercise_detail` (
    `id`                 bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `exercise_record_id` bigint                NOT NULL,
    `exercise_id`        bigint                NOT NULL,
    `set_detail_id`      bigint                NOT NULL,
    `user_id`            bigint                NOT NULL,
    `time`               int                   NULL,
    `intensity`          ENUM('LOOSE', 'NORMAL', 'TIGHT') NULL,
    `created_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`exercise_record_id`) REFERENCES `exercise_record`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercise`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`set_detail_id`) REFERENCES `set_detail`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `exercise_routine` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`      bigint                NOT NULL,
    `name`         varchar(50)           NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `exercise_routine_information` (
    `id`                  bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `exercise_routine_id` bigint                NOT NULL,
    `exercise_id`         bigint                NOT NULL,
    `created_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`exercise_routine_id`) REFERENCES `exercise_routine`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercise`(`id`) ON DELETE CASCADE
);
