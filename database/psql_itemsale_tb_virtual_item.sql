CREATE TABLE tb_virtual_item
(
    id               serial             not null PRIMARY KEY,
    key_loot_gateway VARCHAR(50) UNIQUE not null,
    quantity         DOUBLE PRECISION   not null,
    item_storage_id  INT                not null,
    CONSTRAINT fk_itemstorage
        FOREIGN KEY (item_storage_id)
            REFERENCES tb_item_storage (id)
);
