CREATE DATABASE IF NOT EXISTS Connectivity;

USE Connectivity;

DROP TABLE IF EXISTS `account_details`;
CREATE TABLE `account_details` (
  `user_id` INT(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NULL,
  `secret` varchar(255) NULL,
  `salt` varchar(255) NULL,
  `verification_code` INT(4) ZEROFILL NULL,
  `unique_id` varchar(50) NULL,
  PRIMARY KEY (`user_id`)
);

DROP TABLE IF EXISTS `profile_details`;
CREATE TABLE `profile_details` (
  `profile_id` INT(10) NOT NULL AUTO_INCREMENT,
  `account` INT(10) NOT NULL,
  `fullname` varchar(255) NULL,
  `profile_pic` varchar(255) NULL,
  `location` VARCHAR(255) NULL,
  `profession` varchar(255) NULL,
  `about_me` varchar(255) NULL,
  `screen_name` varchar(255) NULL,
  `interests` varchar(255) NULL,
  `dob` DATE NULL,
  `gender` VARCHAR(10) NULL CHECK (`gender` IN('Male', 'Female', 'Other')),
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`profile_id`),
  UNIQUE(screen_name)
);

DROP TABLE IF EXISTS `preference_details`;
CREATE TABLE `preference_details` (
  `preference_id` INT(10) NOT NULL AUTO_INCREMENT,
  `profile` INT(10) NOT NULL,
  `push_notification` boolean NOT NULL,
  `email_notification` boolean NOT NULL,
  `public` boolean NOT NULL,
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`preference_id`)
);

DROP TABLE IF EXISTS `interest_details`;
CREATE TABLE `interest_details` (
  `interest_id` INT(10) NOT NULL AUTO_INCREMENT,
  `profile` INT(10) NOT NULL,
  `interest` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`interest_id`)
);

DROP TABLE IF EXISTS `connection_details`;
CREATE TABLE `connection_details` (
  `connection_id` INT(10) NOT NULL AUTO_INCREMENT,
  `profile` INT(10) NOT NULL,
  `friend` INT(10) NOT NULL,
  `pending` INT(1) NOT NULL,
  PRIMARY KEY (`connection_id`)
);

DROP TABLE IF EXISTS `follow_details`;
CREATE TABLE `follow_details` (
  `follow_id` INT(10) NOT NULL AUTO_INCREMENT,
  `profile` INT(10) NOT NULL,
  `following` INT(10) NOT NULL,
  PRIMARY KEY (`follow_id`)
);

DROP TABLE IF EXISTS `message_details`;
CREATE TABLE `message_details` (
  `message_id` INT(10) NOT NULL AUTO_INCREMENT,
  `profile` INT(10) NOT NULL,
  `to` INT(10) NOT NULL,
  `subject` VARCHAR(255) NOT NULL,
  `message` VARCHAR(10000) NOT NULL,
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`message_id`)
);

DROP TABLE IF EXISTS `post_details`;
CREATE TABLE `post_details` (
  `post_id` INT(10) NOT NULL AUTO_INCREMENT,
  `profile` INT(10) NOT NULL,
  `post` VARCHAR(10000) NOT NULL,
  `photo` VARCHAR(255) NOT NULL,
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`post_id`)
);

DROP TABLE IF EXISTS `notification_details`;
CREATE TABLE `notification_details` (
  `notification_id` INT(10) NOT NULL AUTO_INCREMENT,
  `profile` INT(10) NOT NULL,
  `friend` INT(10) NOT NULL,
  `text` VARCHAR(10000) NOT NULL,
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`notification_id`)
);
