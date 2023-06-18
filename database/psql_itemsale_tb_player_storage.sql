CREATE TABLE tb_player_storage
(
    id              serial             not null PRIMARY KEY,
    username        VARCHAR(16) UNIQUE not null,
    loot_multiplier DOUBLE PRECISION   not null,
    sale_bonus      DOUBLE PRECISION   not null
);
