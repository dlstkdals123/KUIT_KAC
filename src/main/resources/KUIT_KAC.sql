DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `nickname`     varchar(20)           NOT NULL,
    `password`     varchar(100)          NOT NULL,
    `email`        varchar(50)           NOT NULL,
    `gender`       ENUM('male', 'female') NOT NULL,
    `age`          int                   NOT NULL,
    `height`       int                   NOT NULL,
    `target_weight` double                NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS `exercise`;

CREATE TABLE `exercise` (
    `id`                  bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name`                varchar(40)           NULL,
    `target_muscle_group` varchar(20)           NOT NULL,
    `met_value`           double                NULL,
    `created_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS `food`;

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

DROP TABLE IF EXISTS `set_detail`;

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

DROP TABLE IF EXISTS `diet_template`;

CREATE TABLE `diet_template` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`      bigint                NOT NULL,
    `name`         varchar(30)           NOT NULL,
    `date`         date                  NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `exercise_record`;

CREATE TABLE `exercise_record` (
    `id`            bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`       bigint                NOT NULL,
    `exercise_date` date                  NOT NULL,
    `created_at`    datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `social_dining`;

CREATE TABLE `social_dining` (
    `id`                 bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`            bigint                NOT NULL,
    `type`               ENUM('dining_out', 'drinking') NOT NULL,
    `social_dining_date` date                  NULL,
    `created_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `weight`;

CREATE TABLE `weight` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`      bigint                NOT NULL,
    `weight`       double                NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `exercise_routine`;

CREATE TABLE `exercise_routine` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`      bigint                NOT NULL,
    `name`         varchar(50)           NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `diet`;

CREATE TABLE `diet` (
    `id`               bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`          bigint                NOT NULL,
    `diet_template_id` bigint                NULL,
    `diet_type`        ENUM('record', 'plan', 'AI_plan') NULL,
    `name`             varchar(30)           NULL,
    `diet_date`        date                  NULL,
    `created_at`       datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`diet_template_id`) REFERENCES `diet_template`(`id`) ON DELETE SET NULL
);

DROP TABLE IF EXISTS `meal`;

CREATE TABLE `meal` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `diet_id`      bigint                NOT NULL,
    `food_id`      bigint                NOT NULL,
    `meal_type`    ENUM('breakfast', 'lunch', 'dinner', 'snack') NOT NULL,
    `quantity`     double                NOT NULL,
    `meal_time`    datetime              NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`diet_id`) REFERENCES `diet`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`food_id`) REFERENCES `food`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `exercise_detail`;

CREATE TABLE `exercise_detail` (
    `id`                 bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `exercise_record_id` bigint                NOT NULL,
    `exercise_id`        bigint                NOT NULL,
    `set_detail_id`      bigint                NOT NULL,
    `user_id`            bigint                NOT NULL,
    `time`               int                   NULL,
    `intensity`          ENUM('loose', 'normal', 'tight') NULL,
    `created_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`exercise_record_id`) REFERENCES `exercise_record`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercise`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`set_detail_id`) REFERENCES `set_detail`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `exercise_routine_information`;

CREATE TABLE `exercise_routine_information` (
    `id`                  bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `exercise_routine_id` bigint                NOT NULL,
    `exercise_id`         bigint                NOT NULL,
    `created_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`exercise_routine_id`) REFERENCES `exercise_routine`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercise`(`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `user_information`;

CREATE TABLE `user_information` (
    `user_id`                 bigint NOT NULL PRIMARY KEY,
    `has_diet_experience`     boolean NOT NULL DEFAULT FALSE,
    `diet_fail_reason`        varchar(50) NOT NULL,
    `appetite_type`           ENUM('small', 'big') NOT NULL,
    `weekly_eating_out_count` varchar(10) NOT NULL,
    `eating_out_type`         ENUM('fastfood', 'korean', 'chinese', 'western', 'fried') NOT NULL,
    `diet_velocity`           ENUM('yumyum', 'coach', 'all_in') NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);
