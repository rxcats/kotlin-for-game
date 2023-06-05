create database if not exists sample_app_user01;
create database if not exists sample_app_user02;

create table if not exists `sample_app_user01`.`user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    `updated_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

create table if not exists `sample_app_user01`.`user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    `updated_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;
