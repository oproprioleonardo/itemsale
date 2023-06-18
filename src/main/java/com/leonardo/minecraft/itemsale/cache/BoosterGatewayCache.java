package com.leonardo.minecraft.itemsale.cache;

import com.leonardo.minecraft.itemsale.models.gateway.BoosterGateway;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Optional;

@UtilityClass
public class BoosterGatewayCache {

    public static final HashSet<BoosterGateway> BOOSTERS = new HashSet<>();

    public static void put(BoosterGateway booster) {
        BOOSTERS.add(booster);
    }

    public static void remove(BoosterGateway b) {
        BOOSTERS.remove(b);
    }

    public static void remove(String key) {
        get(key).ifPresent(BOOSTERS::remove);
    }

    public static Optional<BoosterGateway> get(String key) {
        return BOOSTERS.stream().filter(booster -> booster.key().equalsIgnoreCase(key)).findFirst();
    }

    public static boolean has(String key) {
        return BOOSTERS.stream().anyMatch(booster -> booster.key().equalsIgnoreCase(key));
    }




}
