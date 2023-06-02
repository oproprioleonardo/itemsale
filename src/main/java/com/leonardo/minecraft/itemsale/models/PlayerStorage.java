package com.leonardo.minecraft.itemsale.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PlayerStorage implements Serializable {

    private Integer id;
    private String username;
    private Float saleBonus = 0F;
    private Float lootMultiplier = 1F;
    private Set<Boost> boosts = new HashSet<>();
    private ItemStorage itemStorage = new ItemStorage();

    public PlayerStorage() {
    }

    public PlayerStorage(Integer id) {
        this.id = id;
    }

    public PlayerStorage(String username, Float saleBonus, Float lootMultiplier, ItemStorage itemStorage) {
        this.username = username.toLowerCase();
        this.saleBonus = saleBonus;
        this.lootMultiplier = lootMultiplier;
        this.itemStorage = itemStorage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public Float getSaleBonus() {
        return saleBonus;
    }

    public void setSaleBonus(Float saleBonus) {
        this.saleBonus = saleBonus;
    }

    public float getLootMultiplier() {
        return lootMultiplier;
    }

    public void setLootMultiplier(Float lootMultiplier) {
        this.lootMultiplier = lootMultiplier;
    }

    public ItemStorage getItemStorage() {
        return itemStorage;
    }

    public void setItemStorage(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    public Set<Boost> getBoosts() {
        return boosts;
    }

    public void setBoosts(Set<Boost> boosts) {
        this.boosts = boosts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStorage that = (PlayerStorage) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "PlayerStorage{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", saleBonus=" + saleBonus +
                ", lootMultiplier=" + lootMultiplier +
                ", boosts=" + boosts +
                ", itemStorage=" + itemStorage +
                '}';
    }
}
