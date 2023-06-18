package com.leonardo.minecraft.itemsale.listeners;

import com.leonardo.minecraft.itemsale.ItemSale;
import com.leonardo.minecraft.itemsale.internal.services.ItemSaleService;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final ItemSaleService service;

    public QuitListener(ItemSaleService service) {
        this.service = service;
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final PlayerStorage storage = this.service.findByUsername(p.getName());
        if (storage.isPersisted()) {
            this.service.save(storage);
        }
    }
}
