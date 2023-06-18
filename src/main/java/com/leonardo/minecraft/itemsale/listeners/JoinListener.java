package com.leonardo.minecraft.itemsale.listeners;

import com.leonardo.minecraft.itemsale.ItemSale;
import com.leonardo.minecraft.itemsale.internal.services.ItemSaleService;
import com.leonardo.minecraft.itemsale.models.ItemStorage;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final ItemSaleService service;
    private final ItemSale itemSale;

    public JoinListener(ItemSaleService service, ItemSale itemSale) {
        this.service = service;
        this.itemSale = itemSale;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final PlayerStorage storage = this.service.findByUsername(p.getName());
        if (!storage.isPersisted()) {
            storage.setSaleBonus(
                    Float.valueOf(itemSale.getCustomConfig()
                            .getStringList("Boosters.default").stream()
                            .filter(s -> p.hasPermission(s.split(":")[0]))
                            .findFirst()
                            .orElse("0:1.0").split(":")[1])
            );
            final ItemStorage itemStorage = ItemStorage.builder()
                    .owner(storage)
                    .maxCapacity(
                            Double.valueOf(itemSale.getCustomConfig()
                                    .getStringList("Limits.default").stream()
                                    .filter(s -> p.hasPermission(s.split(":")[0]))
                                    .findFirst()
                                    .orElse("0:0")
                                    .split(":")[1])
                    )
                    .build();
            itemStorage.loadDefaultLoot();
            storage.setItemStorage(itemStorage);
            this.service.putOnCache(this.service.save(storage));
        }

    }

}
