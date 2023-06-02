package com.leonardo.minecraft.itemsale.internal.services;

import com.leonardo.minecraft.itemsale.api.repositories.ItemStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.PlayerStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.VirtualItemRepository;

public class ItemSaleService {

    private final PlayerStorageRepository playerStorageRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final VirtualItemRepository virtualItemRepository;

    public ItemSaleService(PlayerStorageRepository playerStorageRepository, ItemStorageRepository itemStorageRepository, VirtualItemRepository virtualItemRepository) {
        this.playerStorageRepository = playerStorageRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.virtualItemRepository = virtualItemRepository;
    }
}
