-- 테이블 삭제 (외래 키 제약 역순)
DROP TABLE IF EXISTS `diet_aifood`;
DROP TABLE IF EXISTS `diet_food`;
DROP TABLE IF EXISTS `diet`;
DROP TABLE IF EXISTS `weight`;
DROP TABLE IF EXISTS `user_information`;
DROP TABLE IF EXISTS `exercise_set`;
DROP TABLE IF EXISTS `exercise_detail`;
DROP TABLE IF EXISTS `routine_exercise`;
DROP TABLE IF EXISTS `routine`;
DROP TABLE IF EXISTS `aifood`;
DROP TABLE IF EXISTS `food`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `exercise`;


-- 사용자 관련 테이블
CREATE TABLE `user` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `nickname`     varchar(20)           NOT NULL UNIQUE,
    `password`     varchar(100)          NOT NULL,
    `email`        varchar(50)           NOT NULL UNIQUE,
    `gender`       ENUM('MALE', 'FEMALE') NOT NULL,
    `age`          int                   NOT NULL,
    `height`       int                   NOT NULL,
    `target_weight` double               NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `user_information` (
    `user_id`                 bigint          NOT NULL PRIMARY KEY,
    `has_diet_experience`     boolean         NOT NULL DEFAULT FALSE,
    `diet_fail_reason`        varchar(50)     NULL,
    `appetite_type`           ENUM('SMALL', 'BIG') NULL,
    `weekly_eating_out_count` varchar(10)     NULL,
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
CREATE TABLE `diet` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`      bigint                NOT NULL,
    `name`         varchar(30)           NULL,
    `diet_type`    ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK', 'TEMPLATE') NOT NULL,
    `diet_entry_type` ENUM('RECORD', 'FASTING', 'PLAN', 'AI_PLAN', 'DINING_OUT', 'DRINKING') NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `food` (
    `id`             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name`           varchar(50)           NOT NULL UNIQUE,
    `unit_type`      varchar(20)           NOT NULL,
    `unit_num`       bigint                NOT NULL,
    `food_type`      ENUM('NORMAL_GRAIN_AND_TUBER', 'NORMAL_FRUIT', 'NORMAL_GRILLED', 'NORMAL_SOUP_AND_TANG', 'NORMAL_KIMCHI', 'NORMAL_NAMUL_AND_SUKCHAE', 'NORMAL_BEANS_AND_NUTS', 'NORMAL_NOODLE_AND_DUMPLING', 'NORMAL_RICE', 'NORMAL_STIR_FRIED', 'NORMAL_BREAD_AND_SNACK', 'NORMAL_FRESH_AND_MUCHIM', 'NORMAL_FISH_AND_MEAT', 'NORMAL_DAIRY_AND_ICECREAM', 'NORMAL_BEVERAGE_AND_TEA', 'NORMAL_SAUCE_AND_SEASONING', 'NORMAL_PICKLE', 'NORMAL_PANCAKE', 'NORMAL_SALTED_SEAFOOD', 'NORMAL_BRAISED', 'NORMAL_PORRIDGE_AND_SOUP', 'NORMAL_STEW_AND_HOT_POT', 'NORMAL_STEAMED', 'NORMAL_VEGETABLE_AND_SEAWEED', 'NORMAL_FRIED', 'PROCESSED_BREAD_AND_SNACK', 'PROCESSED_OTHER_FOOD', 'PROCESSED_AGRICULTURAL', 'PROCESSED_SUGAR', 'PROCESSED_ANIMAL', 'PROCESSED_TOFU_AND_MUK', 'PROCESSED_NOODLE', 'PROCESSED_ICECREAM', 'PROCESSED_SEAFOOD', 'PROCESSED_OIL', 'PROCESSED_MEAT', 'PROCESSED_EGG', 'PROCESSED_DAIRY', 'PROCESSED_BEVERAGE', 'PROCESSED_SAUCE', 'PROCESSED_JAM', 'PROCESSED_PICKLE_AND_BRAISED', 'PROCESSED_SEASONING', 'PROCESSED_ALCOHOL', 'PROCESSED_INSTANT', 'PROCESSED_COCOA_CHOCOLATE', 'PROCESSED_NUTRITION', 'PROCESSED_MEDICAL') NOT NULL,
    `is_processed_food` boolean            NOT NULL DEFAULT FALSE,
    `calorie`        double                NOT NULL DEFAULT 0.0,
    `carbohydrate`   double                NOT NULL DEFAULT 0.0,
    `is_high_carbonhydrate` boolean        NOT NULL DEFAULT FALSE,
    `protein`        double                NOT NULL DEFAULT 0.0,
    `is_high_protein` boolean              NOT NULL DEFAULT FALSE,
    `fat`            double                NOT NULL DEFAULT 0.0,
    `is_high_fat`    boolean               NOT NULL DEFAULT FALSE,
    `sugar`          double                NOT NULL DEFAULT 0.0,
    `score`          int                   NOT NULL,
    `created_at`     datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `aifood` (
    `id`             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`        bigint                NOT NULL,
    `name`           varchar(50)           NOT NULL,
    `unit_type`      varchar(20)           NOT NULL,
    `unit_num`       bigint                NOT NULL,
    `food_type`      ENUM('NORMAL_GRAIN_AND_TUBER', 'NORMAL_FRUIT', 'NORMAL_GRILLED', 'NORMAL_SOUP_AND_TANG', 'NORMAL_KIMCHI', 'NORMAL_NAMUL_AND_SUKCHAE', 'NORMAL_BEANS_AND_NUTS', 'NORMAL_NOODLE_AND_DUMPLING', 'NORMAL_RICE', 'NORMAL_STIR_FRIED', 'NORMAL_BREAD_AND_SNACK', 'NORMAL_FRESH_AND_MUCHIM', 'NORMAL_FISH_AND_MEAT', 'NORMAL_DAIRY_AND_ICECREAM', 'NORMAL_BEVERAGE_AND_TEA', 'NORMAL_SAUCE_AND_SEASONING', 'NORMAL_PICKLE', 'NORMAL_PANCAKE', 'NORMAL_SALTED_SEAFOOD', 'NORMAL_BRAISED', 'NORMAL_PORRIDGE_AND_SOUP', 'NORMAL_STEW_AND_HOT_POT', 'NORMAL_STEAMED', 'NORMAL_VEGETABLE_AND_SEAWEED', 'NORMAL_FRIED', 'PROCESSED_BREAD_AND_SNACK', 'PROCESSED_OTHER_FOOD', 'PROCESSED_AGRICULTURAL', 'PROCESSED_SUGAR', 'PROCESSED_ANIMAL', 'PROCESSED_TOFU_AND_MUK', 'PROCESSED_NOODLE', 'PROCESSED_ICECREAM', 'PROCESSED_SEAFOOD', 'PROCESSED_OIL', 'PROCESSED_MEAT', 'PROCESSED_EGG', 'PROCESSED_DAIRY', 'PROCESSED_BEVERAGE', 'PROCESSED_SAUCE', 'PROCESSED_JAM', 'PROCESSED_PICKLE_AND_BRAISED', 'PROCESSED_SEASONING', 'PROCESSED_ALCOHOL', 'PROCESSED_INSTANT', 'PROCESSED_COCOA_CHOCOLATE', 'PROCESSED_NUTRITION', 'PROCESSED_MEDICAL') NOT NULL,
    `is_processed_food` boolean            NOT NULL DEFAULT FALSE,
    `calorie`        double                NOT NULL DEFAULT 0.0,
    `carbohydrate`   double                NOT NULL DEFAULT 0.0,
    `is_high_carbonhydrate` boolean        NOT NULL DEFAULT FALSE,
    `protein`        double                NOT NULL DEFAULT 0.0,
    `is_high_protein` boolean              NOT NULL DEFAULT FALSE,
    `fat`            double                NOT NULL DEFAULT 0.0,
    `is_high_fat`    boolean               NOT NULL DEFAULT FALSE,
    `sugar`          double                NOT NULL DEFAULT 0.0,
    `score`          int                   NOT NULL,
    `created_at`     datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `diet_food` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `diet_id`      bigint                NOT NULL,
    `food_id`      bigint                NOT NULL,
    `quantity`     double                NOT NULL,
    `diet_time`    datetime              NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`diet_id`) REFERENCES `diet`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`food_id`) REFERENCES `food`(`id`) ON DELETE CASCADE
);

CREATE TABLE `diet_aifood` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `diet_id`      bigint                NOT NULL,
    `aifood_id`    bigint                NOT NULL,
    `quantity`     double                NOT NULL,
    `diet_time`    datetime              NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`diet_id`) REFERENCES `diet`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`aifood_id`) REFERENCES `aifood`(`id`) ON DELETE CASCADE
);


-- 운동 관련 테이블
CREATE TABLE `routine` (
    `id`            bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id`       bigint                NOT NULL,
    `name`          varchar(50)           NULL,
    `exercise_date` date                  NULL,
    `type`          ENUM('RECORD', 'TEMPLATE') NOT NULL,
    `created_at`    datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `exercise` (
    `id`                    bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name`                  varchar(40)           NULL,
    `target_muscle_group`   varchar(20)           NOT NULL,
    `met_value`            double                NULL,
    `created_at`           datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `routine_exercise` (
    `id`           bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `routine_id`   bigint                NOT NULL,
    `exercise_id`  bigint                NOT NULL,
    `created_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`routine_id`) REFERENCES `routine`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`exercise_id`) REFERENCES `exercise`(`id`) ON DELETE CASCADE
);

CREATE TABLE `exercise_detail` (
    `id`                    bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `routine_exercise_id`   bigint                NOT NULL,
    `time`                  int                   NULL,
    `intensity`            ENUM('LOOSE', 'NORMAL', 'TIGHT') NULL,
    `created_at`           datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`routine_exercise_id`) REFERENCES `routine_exercise`(`id`) ON DELETE CASCADE
);

CREATE TABLE `exercise_set` (
    `id`                    bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `routine_exercise_id`   bigint                NOT NULL,
    `count`                 int                   NULL DEFAULT 0,
    `weight_kg`            int                   NULL DEFAULT 0,
    `weight_num`           int                   NULL DEFAULT 0,
    `distance`             int                   NULL DEFAULT 0,
    `time`                 double                NULL DEFAULT 0,
    `set_order`            int                   NULL DEFAULT 0,
    `created_at`           datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           datetime              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`routine_exercise_id`) REFERENCES `routine_exercise`(`id`) ON DELETE CASCADE
);
