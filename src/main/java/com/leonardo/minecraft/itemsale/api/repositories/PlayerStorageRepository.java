package com.leonardo.minecraft.itemsale.api.repositories;

import com.leonardo.minecraft.core.api.database.CrudRepository;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;

public interface PlayerStorageRepository extends CrudRepository<Integer, PlayerStorage> {

    boolean existsUsername(String username);

    PlayerStorage readByUsername(String username);

    void deleteByUsername(String username);

}
