package com.leonardo.minecraft.itemsale.internal.repositories.myqsl;

import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.itemsale.internal.repositories.base.PlayerStorageRepositoryImpl;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerStorageRepositoryMysql extends PlayerStorageRepositoryImpl {

    public PlayerStorageRepositoryMysql(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void createTable() {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS tb_player_storage" +
                    "(" +
                    "id              INT PRIMARY KEY AUTO_INCREMENT," +
                    "username        VARCHAR(16) UNIQUE," +
                    "loot_multiplier REAL," +
                    "sale_bonus      REAL" +
                    ")");
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(PlayerStorage object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("INSERT INTO tb_player_storage" +
                    "(username, loot_multiplier, sale_bonus) " +
                    "VALUES (?,?,?) " +
                    "ON DUPLICATE KEY UPDATE loot_multiplier = ?, sale_bonus = ?");
            this.saveTemplate(object, st);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
