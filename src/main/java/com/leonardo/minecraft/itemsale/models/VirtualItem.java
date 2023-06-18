package com.leonardo.minecraft.itemsale.models;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class VirtualItem implements Serializable {

    private Integer id;
    private String keyLootGateway;
    private ItemStorage itemStorage;
    private Double quantity = 0D;

    public VirtualItem(Integer id) {
        this.id = id;
    }

    @Builder
    public VirtualItem(Integer id, String keyLootGateway, ItemStorage itemStorage, Double quantity) {
        this.id = id;
        this.keyLootGateway = keyLootGateway;
        this.itemStorage = itemStorage;
        this.quantity = quantity;
    }

    public VirtualItem(String keyLootGateway, ItemStorage itemStorage) {
        this.keyLootGateway = keyLootGateway;
        this.itemStorage = itemStorage;
    }

    public void setQuantity(Double quantity) {
        if (this.quantity >= 0D)
            this.quantity = Math.floor(quantity);
    }

    public void addQuantity(Double quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(Double quantity) {
        this.setQuantity(Math.max(this.quantity - quantity, 0D));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualItem that = (VirtualItem) o;
        return Objects.equals(id, that.id) && Objects.equals(keyLootGateway, that.keyLootGateway) && Objects.equals(itemStorage, that.itemStorage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyLootGateway, itemStorage);
    }

    @Override
    public String toString() {
        return "VirtualItem{" +
                "id=" + id +
                ", keyLootGateway='" + keyLootGateway + '\'' +
                ", itemStorage=" + itemStorage +
                ", quantity=" + quantity +
                '}';
    }
}
