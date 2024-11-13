-- USER
create table USER (user_id varchar(30), user_name varchar(50), email varchar(50), password varchar(30), phone_number varchar(15), gender varchar(30) , dob date, full_name varchar(30), bio varchar(140), profile_picture varchar(256), is_private boolean, created_at timestamp null, last_modified timestamp null, is_active boolean);

ALTER TABLE user ADD COLUMN `website` VARCHAR(100) NULL DEFAULT NULL AFTER `is_active`;

ALTER TABLE user ADD COLUMN `is_channel` boolean DEFAULT false AFTER `website`;


ALTER TABLE `tot`.`USER` 
ADD COLUMN `cover_picture` VARCHAR(256) NULL DEFAULT NULL AFTER `profile_picture`;


-- FOLLOW
create table FOLLOW (src_user_id varchar(30), tgt_user_id varchar(30), accepted boolean, created_at timestamp null, accepted_at timestamp null);
alter table FOLLOW add unique (src_user_id, tgt_user_id);

create table user_stats (user_id varchar(30), posts_count long, followers_count long, following_count long);
alter table user_stats add unique (user_id);

create table USER_PREFERENCE (user_id varchar(30), allow_notifications boolean, allow_post_notifications boolean, allow_follow_notifications boolean);
alter table USER_PREFERENCE add unique (user_id);

create table USER_DEVICES (user_id varchar(30), device_fcm_token varchar(200), register_time timestamp);
alter table USER_DEVICES add unique (device_fcm_token);

--------start trigger for inserting into user_stats--------
DELIMITER $$

CREATE TRIGGER follow_update_trigger
    AFTER UPDATE
    ON follow FOR EACH ROW
BEGIN
    if(new.accepted = 1) then
    	INSERT INTO user_stats (user_id, followers_count, posts_count, following_count)
		VALUES (new.src_user_id, 0, 0, 1)
		ON DUPLICATE KEY UPDATE
  			following_count=following_count+1;
  		INSERT INTO user_stats (user_id, followers_count, posts_count, following_count)
		VALUES (new.tgt_user_id, 1, 0, 0)
		ON DUPLICATE KEY UPDATE
  			followers_count=followers_count+1;
    end if;
END$$

DELIMITER ;

----------------------new trigger-----------------------

DELIMITER $$

CREATE TRIGGER follow_delete_trigger
    AFTER DELETE
    ON follow FOR EACH ROW
BEGIN
	if(old.accepted = 1) then
    	UPDATE user_stats SET
  			following_count=following_count-1 where user_id = old.src_user_id;
  		UPDATE user_stats SET
  			followers_count=followers_count-1 where user_id = old.tgt_user_id;
  	end if;
END$$

DELIMITER ;

----------trigger end-------

  
CREATE TABLE `tags` (
  `tag` varchar(32) NOT NULL,
  `post_count` int DEFAULT '0',
  PRIMARY KEY (`tag`));
  
CREATE TABLE `tot`.`interests_master` (
  `interest_tag` VARCHAR(45) NOT NULL,
  `interest_name` VARCHAR(100) NOT NULL,
  `picture` VARCHAR(256) NULL,
  PRIMARY KEY (`interest_tag`));




