package com.leonardo.minecraft.itemsale.cache;

import com.leonardo.minecraft.itemsale.models.gateway.LootGateway;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Optional;

@UtilityClass
public class LootGatewayCache {

    private static final HashSet<LootGateway> LOOT_GATEWAYS = new HashSet<>();

    public static void put(LootGateway loot) {
        LOOT_GATEWAYS.add(loot);
    }

    public static void remove(LootGateway loot) {
        LOOT_GATEWAYS.remove(loot);
    }

    public static void remove(String key) {
        get(key).ifPresent(LOOT_GATEWAYS::remove);
    }

    public static boolean has(String key) {
        return LOOT_GATEWAYS.stream().anyMatch(loot -> loot.key().equalsIgnoreCase(key));
    }

    public static Optional<LootGateway> get(String key) {
        return LOOT_GATEWAYS.stream().filter(loot -> loot.key().equalsIgnoreCase(key)).findFirst();
    }

}
