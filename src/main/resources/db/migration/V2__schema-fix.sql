CREATE TABLE IF NOT EXISTS player
(
    id_player int          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username  varchar(20)  not null,
    password  varchar(255) not null,
    score     int
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE player
    MODIFY password VARCHAR(255) NULL;

ALTER TABLE player
    MODIFY score INT NOT NULL;

ALTER TABLE player
    MODIFY username VARCHAR(255);

ALTER TABLE player
    MODIFY username VARCHAR(255) NULL;

CREATE TABLE IF NOT EXISTS word
(
    id_word int         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    word    varchar(30) not null
)