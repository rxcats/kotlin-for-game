create database if not exists route_commondb;
create database if not exists route_userdb1;
create database if not exists route_userdb2;

create table if not exists `route_commondb`.`common_user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `shard_no`   int          DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

create table if not exists `route_userdb1`.`user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

create table if not exists `route_userdb2`.`user`
(
    `user_id`    varchar(45) NOT NULL,
    `nickname`   varchar(200) DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;
