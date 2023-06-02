package com.leonardo.minecraft.itemsale.internal.repositories.psql;

import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.itemsale.internal.repositories.base.VirtualItemRepositoryImpl;
import com.leonardo.minecraft.itemsale.models.VirtualItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VirtualItemRepositoryPsql extends VirtualItemRepositoryImpl {

    public VirtualItemRepositoryPsql(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void createTable() {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS tb_virtual_item" +
                    "(" +
                    "id              SERIAL PRIMARY KEY," +
                    "factual_item_id VARCHAR(50)," +
                    "quantity        DOUBLE PRECISION," +
                    "item_storage_id INT REFERENCES tb_item_storage (id)" +
                    ");");

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
