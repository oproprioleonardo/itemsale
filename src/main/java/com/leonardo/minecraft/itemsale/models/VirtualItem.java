package com.leonardo.minecraft.itemsale.models;

import java.io.Serializable;
import java.util.Objects;

public class VirtualItem implements Serializable {

    private Integer id;
    private String factualItemId;
    private ItemStorage itemStorage;
    private Double quantity = 0D;

    public VirtualItem() {
    }

    public VirtualItem(Integer id) {
        this.id = id;
    }

    public VirtualItem(String factualItemId) {
        this.factualItemId = factualItemId;
    }

    public VirtualItem(String factualItemId, ItemStorage itemStorage) {
        this.factualItemId = factualItemId;
        this.itemStorage = itemStorage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFactualItemId() {
        return factualItemId;
    }

    public void setFactualItemId(String factualItemId) {
        this.factualItemId = factualItemId;
    }

    public ItemStorage getItemStorage() {
        return itemStorage;
    }

    public void setItemStorage(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    public Double getQuantity() {
        return Math.floor(quantity);
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
        return Objects.equals(id, that.id) && Objects.equals(factualItemId, that.factualItemId) && Objects.equals(itemStorage, that.itemStorage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, factualItemId, itemStorage);
    }

    @Override
    public String toString() {
        return "VirtualItem{" +
                "id=" + id +
                ", factualItemId='" + factualItemId + '\'' +
                ", itemStorage=" + itemStorage +
                ", quantity=" + quantity +
                '}';
    }
}
