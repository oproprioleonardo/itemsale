CREATE TABLE tb_virtual_item
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    factual_item_id VARCHAR(50) UNIQUE not null,
    quantity        DOUBLE             not null,
    item_storage_id INT                not null REFERENCES tb_item_storage (id)
);
