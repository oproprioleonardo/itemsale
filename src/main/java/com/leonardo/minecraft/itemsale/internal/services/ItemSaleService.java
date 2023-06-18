package com.leonardo.minecraft.itemsale.internal.services;

import com.leonardo.minecraft.itemsale.api.repositories.ItemStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.PlayerStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.VirtualItemRepository;
import com.leonardo.minecraft.itemsale.models.ItemStorage;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;
import com.leonardo.minecraft.itemsale.models.VirtualItem;

import java.util.HashSet;
import java.util.Set;

public class ItemSaleService {

    private final PlayerStorageRepository playerStorageRepository;
    private final ItemStorageRepository itemStorageRepository;
    private final VirtualItemRepository virtualItemRepository;
    private final Set<PlayerStorage> cache = new HashSet<>();

    public ItemSaleService(PlayerStorageRepository playerStorageRepository, ItemStorageRepository itemStorageRepository, VirtualItemRepository virtualItemRepository) {
        this.playerStorageRepository = playerStorageRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.virtualItemRepository = virtualItemRepository;
    }

    public PlayerStorage findByUsername(String username) {
        return cache.stream()
                .filter(playerStorage -> playerStorage.getUsername().equals(username.toLowerCase()))
                .findFirst()
                .orElseGet(() -> {
                    final PlayerStorage storage = playerStorageRepository.readByUsername(username.toLowerCase());
                    if (!storage.isPersisted())
                        return storage;
                    final ItemStorage itemStorage = itemStorageRepository.readByPlayerStorage(storage);
                    final Set<VirtualItem> virtualItems = virtualItemRepository.findAllByItemStorage(itemStorage);
                    itemStorage.setLoot(virtualItems);
                    itemStorage.removeUselessLoot();
                    itemStorage.loadDefaultLoot();
                    storage.setItemStorage(itemStorage);
                    cache.add(storage);
                    return storage;
                });
    }

    public boolean existsUsername(String username) {
        return cache
                .stream()
                .anyMatch(playerStorage -> playerStorage.getUsername().equals(username.toLowerCase()))
                || playerStorageRepository.existsUsername(username);
    }

    public PlayerStorage save(PlayerStorage storage) {
        this.cache.removeIf(playerStorage -> playerStorage.getId().equals(storage.getId()));
        this.playerStorageRepository.save(storage);
        this.itemStorageRepository.save(storage.getItemStorage());
        this.virtualItemRepository.saveAll(storage.getItemStorage().getLoot());
        return storage;
    }

    public void putOnCache(PlayerStorage storage) {
        this.cache.add(storage);
    }
}
