CREATE TABLE IF NOT EXISTS player (
    id_player int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username varchar(20) not null,
    password varchar(255) not null,
    score int
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

