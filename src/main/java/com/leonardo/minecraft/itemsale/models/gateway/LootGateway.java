package com.leonardo.minecraft.itemsale.models.gateway;

import com.leonardo.minecraft.itemsale.models.enums.LootType;
import fr.minuskube.inv.ClickableItem;
import lombok.Builder;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class LootGateway {

    private final String key;
    private final LootType type;
    private final ItemStack drop;
    private final EntityType entityType;
    private final Double unitPrice;
    private final ClickableItem menuItem;
    private final boolean canCollect;
    private final boolean canSell;

    @Builder
    public LootGateway(String key, LootType type, ItemStack drop, EntityType entityType, Double unitPrice, ClickableItem menuItem, boolean canCollect, boolean canSell) {
        this.key = key;
        this.type = type;
        this.drop = drop;
        this.entityType = entityType;
        this.unitPrice = unitPrice;
        this.menuItem = menuItem;
        this.canCollect = canCollect;
        this.canSell = canSell;
    }

    public EntityType entityType() {
        return entityType;
    }

    public String key() {
        return key;
    }

    public LootType type() {
        return type;
    }

    public ItemStack loot() {
        return drop;
    }

    public Double unitPrice() {
        return unitPrice;
    }

    public ClickableItem menuItem() {
        return menuItem;
    }

    public boolean canCollect() {
        return canCollect;
    }

    public boolean canSell() {
        return canSell;
    }

}
