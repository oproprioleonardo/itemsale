CREATE TABLE tb_item_storage
(
    id           SERIAL           not null PRIMARY KEY,
    max_capacity DOUBLE PRECISION not null,
    auto_sell    bool             not null,
    owner_id     INT UNIQUE       not null,
    CONSTRAINT fk_owner
        FOREIGN KEY (owner_id)
            REFERENCES tb_player_storage (id)
);
