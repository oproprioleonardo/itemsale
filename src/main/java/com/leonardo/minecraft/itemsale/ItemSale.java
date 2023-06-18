package com.leonardo.minecraft.itemsale;

import com.leonardo.minecraft.core.Core;
import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.core.api.database.CrudRepository;
import com.leonardo.minecraft.core.config.DatabaseConfig;
import com.leonardo.minecraft.core.config.DatabaseName;
import com.leonardo.minecraft.itemsale.api.repositories.ItemStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.PlayerStorageRepository;
import com.leonardo.minecraft.itemsale.api.repositories.VirtualItemRepository;
import com.leonardo.minecraft.itemsale.internal.repositories.myqsl.ItemStorageRepositoryMysql;
import com.leonardo.minecraft.itemsale.internal.repositories.myqsl.PlayerStorageRepositoryMysql;
import com.leonardo.minecraft.itemsale.internal.repositories.myqsl.VirtualItemRepositoryMysql;
import com.leonardo.minecraft.itemsale.internal.repositories.psql.ItemStorageRepositoryPsql;
import com.leonardo.minecraft.itemsale.internal.repositories.psql.PlayerStorageRepositoryPsql;
import com.leonardo.minecraft.itemsale.internal.repositories.psql.VirtualItemRepositoryPsql;
import com.leonardo.minecraft.itemsale.internal.services.ItemSaleService;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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

    }

    @Override
    public void onDisable() {

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


}
