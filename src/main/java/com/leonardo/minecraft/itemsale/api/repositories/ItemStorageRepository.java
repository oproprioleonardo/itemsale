package com.leonardo.minecraft.itemsale.api.repositories;

import com.leonardo.minecraft.core.api.database.CrudRepository;
import com.leonardo.minecraft.itemsale.models.ItemStorage;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;

public interface ItemStorageRepository extends CrudRepository<Integer, ItemStorage> {

    ItemStorage readByPlayerStorage(PlayerStorage playerStorage);
}
