package com.leonardo.minecraft.itemsale.models;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class PlayerStorage implements Serializable {

    private Integer id;
    private String username;
    private Float saleBonus = 0F;
    private Float lootMultiplier = 1F;
    private Set<Boost> boosts = new HashSet<>();
    private ItemStorage itemStorage = new ItemStorage();

    public PlayerStorage(Integer id) {
        this.id = id;
    }

    @Builder
    public PlayerStorage(Integer id, String username, Float saleBonus, Float lootMultiplier, ItemStorage itemStorage, Set<Boost> boosts) {
        this.id = id;
        this.username = username.toLowerCase();
        this.saleBonus = saleBonus;
        this.lootMultiplier = lootMultiplier;
        this.boosts = boosts;
        this.itemStorage = itemStorage;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public boolean isPersisted() {
        return id != 0;
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
