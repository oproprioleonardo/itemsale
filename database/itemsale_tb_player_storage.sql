CREATE TABLE tb_player_storage
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(16) UNIQUE not null,
    loot_multiplier REAL               not null,
    sale_bonus      REAL               not null
);
