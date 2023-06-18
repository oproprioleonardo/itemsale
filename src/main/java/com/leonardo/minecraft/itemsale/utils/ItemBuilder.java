/*
 *
 *   Copyright © 2020 Dev_NullPointer
 *   All rights reserved.
 *
 *   Any redistribution or reproduction of this software, in any form, is prohibited.
 *
 *   -- Discord: NullPointerException#7966 --
 *
 */

package com.leonardo.minecraft.itemsale.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {

    private ItemStack itemStack;
    private Boolean closeable = false;
    private Boolean editable = true;
    private Boolean cancellable = false;

    public ItemBuilder() {
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack item) {
        this.itemStack = item;
    }

    public ItemBuilder(Material m, int amount, short data) {
        this.itemStack = new ItemStack(m, amount, data);
    }

    public ItemBuilder setType(Material material) {
        if (itemStack == null) itemStack = new ItemStack(material);
        else itemStack.setType(material);
        return this;
    }

    public Boolean isCloseable() {
        return closeable;
    }

    public Boolean isEditable() {
        return editable;
    }

    public ItemBuilder setCloseable(Boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public ItemBuilder setCancel(Boolean b) {
        this.cancellable = b;
        return this;
    }

    public Boolean isCancellable() {
        return cancellable;
    }

    public ItemBuilder setEditable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    public ItemBuilder durability(Integer durability) {
        itemStack.setDurability(Short.parseShort(durability.toString()));
        return this;
    }

    public ItemBuilder name(String name) {
        final ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... line) {
        final ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(Arrays.asList(line))
                .stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        final ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(Integer amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, Integer level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder hideAttributes() {
        final ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder color(Color color) {
        if (!itemStack.getType().name().contains("LEATHER_")) return this;
        final LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(color);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder glow() {
        final ItemMeta im = itemStack.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder unGlow() {
        final ItemMeta im = itemStack.getItemMeta();
        im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.removeEnchant(Enchantment.DURABILITY);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemMeta getItemMeta() {
        return itemStack.getItemMeta();
    }

    public ItemBuilder setItemMeta(ItemMeta im) {
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLines(List<String> lore) {
        final ItemMeta meta = itemStack.getItemMeta();
        final List<String> str = new ArrayList<>(meta.getLore());
        str.addAll(lore);
        meta.setLore(str);
        itemStack.setItemMeta(meta);
        return this;
    }


    public ItemBuilder setSkullOwner(String owner) {
        if (owner == null || owner.isEmpty()) return this;
        try {
            final SkullMeta im = (SkullMeta) itemStack.getItemMeta();
            im.setOwner(owner);
            itemStack.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder setSkullUrl(String url) {
        try {
            if (url == null || url.isEmpty()) return this;
            final SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
            final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            final byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            try {
                final Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (Exception ignored) {
            }
            itemStack.setItemMeta(skullMeta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}