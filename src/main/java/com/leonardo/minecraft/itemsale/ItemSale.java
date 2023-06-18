package com.leonardo.minecraft.itemsale;

import com.leonardo.minecraft.core.Core;
import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.core.api.database.CrudRepository;
import com.leonardo.minecraft.core.config.DatabaseConfig;
import com.leonardo.minecraft.core.config.DatabaseName;
import com.leonardo.minecraft.itemsale.api.repositories.ItemStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.PlayerStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.VirtualItemRepository;
import com.leonardo.minecraft.itemsale.cache.LimitGatewayCache;
import com.leonardo.minecraft.itemsale.cache.LootGatewayCache;
import com.leonardo.minecraft.itemsale.internal.repositories.myqsl.ItemStorageRepositoryMysql;
import com.leonardo.minecraft.itemsale.internal.repositories.myqsl.PlayerStorageRepositoryMysql;
import com.leonardo.minecraft.itemsale.internal.repositories.myqsl.VirtualItemRepositoryMysql;
import com.leonardo.minecraft.itemsale.internal.repositories.psql.ItemStorageRepositoryPsql;
import com.leonardo.minecraft.itemsale.internal.repositories.psql.PlayerStorageRepositoryPsql;
import com.leonardo.minecraft.itemsale.internal.repositories.psql.VirtualItemRepositoryPsql;
import com.leonardo.minecraft.itemsale.internal.services.ItemSaleService;
import com.leonardo.minecraft.itemsale.listeners.JoinListener;
import com.leonardo.minecraft.itemsale.listeners.QuitListener;
import com.leonardo.minecraft.itemsale.models.enums.LootType;
import com.leonardo.minecraft.itemsale.models.gateway.BoosterGateway;
import com.leonardo.minecraft.itemsale.models.gateway.LimitGateway;
import com.leonardo.minecraft.itemsale.models.gateway.LootGateway;
import com.leonardo.minecraft.itemsale.utils.ItemBuilder;
import com.leonardo.minecraft.itemsale.utils.ItemSaleUtils;
import fr.minuskube.inv.ClickableItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public class ItemSale extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;
    private Core core;
    private PlayerStorageRepository playerStorageRepository;
    private ItemStorageRepository itemStorageRepository;
    private VirtualItemRepository virtualItemRepository;
    private ItemSaleService itemSaleService;


    @Override
    public void onEnable() {
        this.core = getPlugin(Core.class);
        this.customConfigFile = new File(getDataFolder(), "config.yml");
        this.customConfig = this.provideConfig(this.customConfigFile);
        this.provideRepositories(this.core.getConnectionProvider(), this.core.getDatabaseConfig());
        this.createTables(Arrays.asList(
                this.playerStorageRepository,
                this.itemStorageRepository,
                this.virtualItemRepository
        ));
        this.itemSaleService = this.provideItemSaleService(
                this.playerStorageRepository,
                this.itemStorageRepository,
                this.virtualItemRepository
        );
        this.populateLootGatewayCache();
        this.populateLimitGatewayCache();
        this.populateBoosterGatewayCache();
        this.registerListeners();
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(this.itemSaleService, this), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(this.itemSaleService), this);
    }

    private FileConfiguration provideConfig(final File customConfigFile) {
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        return YamlConfiguration.loadConfiguration(customConfigFile);
    }

    private void provideRepositories(final ConnectionProvider connectionProvider, final DatabaseConfig databaseConfig) {
        if (databaseConfig.getDbname() == DatabaseName.POSTGRES) {
            this.playerStorageRepository = new PlayerStorageRepositoryPsql(connectionProvider);
            this.itemStorageRepository = new ItemStorageRepositoryPsql(connectionProvider);
            this.virtualItemRepository = new VirtualItemRepositoryPsql(connectionProvider);
        } else {
            this.playerStorageRepository = new PlayerStorageRepositoryMysql(connectionProvider);
            this.itemStorageRepository = new ItemStorageRepositoryMysql(connectionProvider);
            this.virtualItemRepository = new VirtualItemRepositoryMysql(connectionProvider);
        }
    }

    private void createTables(final List<CrudRepository<?, ?>> repositories) {
        repositories.forEach(CrudRepository::createTable);
    }

    private ItemSaleService provideItemSaleService(
            final PlayerStorageRepository playerStorageRepo,
            final ItemStorageRepository itemStorageRepo,
            final VirtualItemRepository virtualItemRepo) {
        return new ItemSaleService(playerStorageRepo, itemStorageRepo, virtualItemRepo);
    }

    private void populateLootGatewayCache() {
        customConfig.getConfigurationSection("Drops").getKeys(false)
                .forEach(s -> {
                    final String path = "Drops." + s + ".";
                    final LootGateway loot = LootGateway.builder()
                            .key(s)
                            .type(LootType.valueOf(customConfig.getString(path + "type")))
                            .drop(
                                    new ItemStack(customConfig.getInt(path + "id"),
                                            1,
                                            (short) customConfig.getInt(path + "data")
                                    )
                            )
                            .entityType(
                                    customConfig.get(path + "mob") != null ?
                                            EntityType.valueOf(customConfig.getString(path + "mob")) : null
                            )
                            .unitPrice(
                                    customConfig.get(path + "unit-sales-value") instanceof Double ?
                                            customConfig.getDouble(path + "unit-sales-value") :
                                            ItemSaleUtils.get(customConfig.getString(path + "unit-sales-value").split(","))
                            )
                            .menuItem(
                                    ClickableItem.empty(
                                            new ItemBuilder(
                                                    Material.getMaterial(customConfig.getInt(path + "drop-item-menu.id")),
                                                    customConfig.getInt(path + "drop-item-menu.amount"),
                                                    (short) customConfig.getInt(path + "drop-item-menu.data")
                                            )
                                                    .name(customConfig.getString(path + "drop-item-menu.name"))
                                                    .lore(customConfig.getStringList(path + "drop-item-menu.lore"))
                                                    .build()
                                    )
                            )
                            .canCollect(customConfig.getBoolean(path + "canCollect"))
                            .canSell(customConfig.getBoolean(path + "canSell"))
                            .build();

                    LootGatewayCache.put(loot);

                });
    }

    private void populateLimitGatewayCache() {
        customConfig.getConfigurationSection("Limits.list.").getKeys(false)
                .forEach(s -> {
                    final String path = "Limits.list." + s + ".";
                    LimitGateway limit = LimitGateway.builder()
                            .value(customConfig.getDouble(path + "value"))
                            .key(s)
                            .permission(customConfig.getString(path + "permission"))
                            .itemStack(
                                    new ItemBuilder(
                                            Material.getMaterial(customConfig.getInt(path + "id")),
                                            1,
                                            (short) customConfig.getInt(path + "data")
                                    )
                                            .name(customConfig.getString(path + "name"))
                                            .lore(customConfig.getStringList(path + "lore"))
                                            .build()
                            )
                            .build();
                    LimitGatewayCache.put(limit);
                });
    }

    private void populateBoosterGatewayCache() {
        customConfig.getConfigurationSection("Boosters.list").getKeys(false)
                .forEach(s -> {
                    final String path = "Boosters.list." + s + ".";
                    BoosterGateway.builder()
                            .key(s)
                            .permission(customConfig.getString(path + "permission"))
                            .multiplier(customConfig.getDouble(path + "multiplier"))
                            .time(customConfig.getLong(path + "time"))
                            .item(
                                    new ItemBuilder(
                                            Material.getMaterial(customConfig.getInt(path + "id")),
                                            1,
                                            (short) customConfig.getInt(path + "data")
                                    )
                                            .name(customConfig.getString(path + "name"))
                                            .lore(customConfig.getStringList(path + "lore"))
                                            .build()
                            )
                            .build();
                });
    }


}
