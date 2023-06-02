package com.leonardo.minecraft.itemsale.internal.repositories.myqsl;

import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.itemsale.internal.repositories.base.VirtualItemRepositoryImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VirtualItemRepositoryMysql extends VirtualItemRepositoryImpl {

    public VirtualItemRepositoryMysql(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void createTable() {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS tb_virtual_item" +
                    "(" +
                    "id              INT PRIMARY KEY AUTO_INCREMENT," +
                    "factual_item_id VARCHAR(50)," +
                    "quantity        DOUBLE," +
                    "item_storage_id INT REFERENCES tb_item_storage (id)" +
                    ");");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
