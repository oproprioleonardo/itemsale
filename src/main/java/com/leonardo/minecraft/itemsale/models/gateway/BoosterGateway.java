package com.leonardo.minecraft.itemsale.models.gateway;

import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BoosterGateway {

    private final String key;
    private final ItemStack item;
    private final Double multiplier;
    private final Long time;
    private final String permission;

    @Builder
    public BoosterGateway(String key, ItemStack item, Double multiplier, Long time, String permission) {
        this.key = key;
        this.item = item;
        this.multiplier = multiplier;
        this.time = time;
        this.permission = permission;
    }

    public String key() {
        return key;
    }

    public ItemStack item() {
        return item;
    }

    public Double multiplier() {
        return multiplier;
    }

    public Long time() {
        return time;
    }

    public String permission() {
        return permission;
    }

    public boolean needAuthorization() {
        return permission.equalsIgnoreCase("");
    }

    public boolean hasAuthorization(Player p) {
        if (!needAuthorization()) return true;
        return p.hasPermission(this.permission());
    }

}
