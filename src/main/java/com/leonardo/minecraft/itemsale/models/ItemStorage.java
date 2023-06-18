package com.leonardo.minecraft.itemsale.models;

import com.leonardo.minecraft.itemsale.cache.LootGatewayCache;
import com.leonardo.minecraft.itemsale.models.gateway.LootGateway;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class ItemStorage implements Serializable {

    private Integer id;
    private Double maxCapacity;
    private PlayerStorage owner;
    private boolean autoSell = false;
    private Set<VirtualItem> loot = new HashSet<>();

    public ItemStorage(Integer id) {
        this.id = id;
    }

    @Builder
    public ItemStorage(Integer id, Double maxCapacity, PlayerStorage owner, boolean autoSell, Set<VirtualItem> loot) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.owner = owner;
        this.autoSell = autoSell;
        this.loot = loot;
    }

    public ItemStorage(Double maxCapacity, PlayerStorage owner) {
        this.maxCapacity = maxCapacity;
        this.owner = owner;
    }

    public ItemStorage(Double maxCapacity, PlayerStorage owner, Set<VirtualItem> loot) {
        this.maxCapacity = maxCapacity;
        this.owner = owner;
        this.loot = loot;
    }

    public void setLoot(Set<VirtualItem> loot) {
        this.loot = loot;
    }

    public void addLoot(VirtualItem virtualItem) {
        virtualItem.setItemStorage(this);
        this.loot.add(virtualItem);
    }

    public void removeUselessLoot() {
        this.loot = this.loot.stream()
                .filter(virtualItem -> LootGatewayCache.LOOT_GATEWAYS.stream()
                        .map(LootGateway::key)
                        .anyMatch(s -> s.equals(virtualItem.getKeyLootGateway()))
                )
                .collect(Collectors.toSet());
    }

    public void loadDefaultLoot() {
        LootGatewayCache.LOOT_GATEWAYS.stream()
                .filter(lootGateway -> this.getLoot()
                        .stream()
                        .noneMatch(i -> i.getKeyLootGateway().equals(lootGateway.key()))
                )
                .forEach(loot -> this.addLoot(new VirtualItem(loot.key(), this)));
    }

    public void reset() {
        this.loot.forEach(virtualItem -> virtualItem.setQuantity(0D));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStorage that = (ItemStorage) o;
        return Objects.equals(id, that.id) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }

    @Override
    public String toString() {
        return "ItemStorage{" +
                "id=" + id +
                ", maxCapacity=" + maxCapacity +
                ", owner=" + owner +
                ", autoSell=" + autoSell +
                ", loot=" + loot +
                '}';
    }
}
