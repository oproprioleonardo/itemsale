package com.leonardo.minecraft.itemsale.cache;

import com.leonardo.minecraft.itemsale.models.gateway.LimitGateway;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Optional;

@UtilityClass
public class LimitGatewayCache {

    private static final HashSet<LimitGateway> LIMIT_GATEWAYS = new HashSet<>();

    public static void put(LimitGateway limit) {
        LIMIT_GATEWAYS.add(limit);
    }

    public static void remove(LimitGateway limit) {
        LIMIT_GATEWAYS.remove(limit);
    }

    public static void remove(String key) {
        get(key).ifPresent(LIMIT_GATEWAYS::remove);
    }

    public static boolean has(String key) {
        return LIMIT_GATEWAYS.stream().anyMatch(limit -> limit.key().equalsIgnoreCase(key));
    }

    public static Optional<LimitGateway> get(String key) {
        return LIMIT_GATEWAYS.stream().filter(limit -> limit.key().equalsIgnoreCase(key)).findFirst();
    }

}
