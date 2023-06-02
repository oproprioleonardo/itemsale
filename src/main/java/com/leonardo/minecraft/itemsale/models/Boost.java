package com.leonardo.minecraft.itemsale.models;

public class Boost {

    private Double multiplier;
    private Long time;

    public Boost(Double multiplier, Long time) {
        this.multiplier = multiplier;
        this.time = time;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
