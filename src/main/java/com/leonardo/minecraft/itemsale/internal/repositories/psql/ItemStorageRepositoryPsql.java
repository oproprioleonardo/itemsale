package com.leonardo.minecraft.itemsale.internal.repositories.psql;

import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.itemsale.internal.repositories.base.ItemStorageRepositoryImpl;
import com.leonardo.minecraft.itemsale.models.ItemStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemStorageRepositoryPsql extends ItemStorageRepositoryImpl {

    public ItemStorageRepositoryPsql(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void createTable() {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS tb_item_storage" +
                    "(" +
                    "id           SERIAL PRIMARY KEY," +
                    "max_capacity DOUBLE PRECISION," +
                    "auto_sell    BOOLEAN," +
                    "owner_id     INT UNIQUE REFERENCES tb_player_storage (id)" +
                    ")");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(ItemStorage object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("INSERT INTO tb_item_storage(max_capacity, auto_sell, owner_id) " +
                    "VALUES (?,?,?) ON CONFLICT(owner_id) UPDATE SET max_capacity = ?, auto_sell = ?");
            this.saveTemplate(object, st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
