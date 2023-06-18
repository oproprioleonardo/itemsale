CREATE TABLE tb_item_storage
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    max_capacity DOUBLE     not null,
    auto_sell    BOOLEAN    not null,
    owner_id     INT UNIQUE not null REFERENCES tb_player_storage (id)
);
