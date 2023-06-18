package com.leonardo.minecraft.itemsale.models.gateway;

import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LimitGateway {

    private final String key;
    private final ItemStack itemStack;
    private final Double value;
    private final String permission;

    @Builder
    public LimitGateway(String key, ItemStack itemStack, Double value, String permission) {
        this.key = key;
        this.itemStack = itemStack;
        this.value = value;
        this.permission = permission;
    }

    public String key() {
        return key;
    }

    public boolean hasAuthorization(Player player) {
        if (!needAuthorization()) return true;
        return player.hasPermission(permission);
    }

    public ItemStack itemStack() {
        return itemStack;
    }

    public Double value() {
        return value;
    }

    public String permission() {
        return permission;
    }

    private boolean needAuthorization() {
        return permission.equalsIgnoreCase("");
    }


}
