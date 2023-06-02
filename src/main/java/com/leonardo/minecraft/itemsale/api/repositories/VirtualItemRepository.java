package com.leonardo.minecraft.itemsale.api.repositories;

import com.leonardo.minecraft.core.api.database.CrudRepository;
import com.leonardo.minecraft.itemsale.models.ItemStorage;
import com.leonardo.minecraft.itemsale.models.VirtualItem;

import java.util.Set;

public interface VirtualItemRepository extends CrudRepository<Integer, VirtualItem> {

    Set<VirtualItem> findAllByItemStorage(ItemStorage itemStorage);

}
