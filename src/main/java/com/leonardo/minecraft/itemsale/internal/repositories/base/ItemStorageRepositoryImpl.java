package com.leonardo.minecraft.itemsale.internal.repositories.base;

import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.itemsale.api.repositories.ItemStorageRepository;
import com.leonardo.minecraft.itemsale.models.ItemStorage;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public abstract class ItemStorageRepositoryImpl implements ItemStorageRepository {

    private final ConnectionProvider connectionProvider;

    public ItemStorageRepositoryImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    @Override
    public Set<ItemStorage> findAll(Map<String, String> whereSearch, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public Set<ItemStorage> findAll(Map<String, String> whereSearch, Integer limit) {
        return null;
    }

    @Override
    public Set<ItemStorage> findAll(Map<String, String> whereSearch) {
        return null;
    }

    @Override
    public Set<ItemStorage> findAll() {
        return null;
    }

    @Override
    public void saveAll(Set<ItemStorage> objects) {
        objects.forEach(this::save);
    }

    protected void saveTemplate(ItemStorage object, PreparedStatement st) throws SQLException {
        st.setDouble(1, object.getMaxCapacity());
        st.setBoolean(2, object.isAutoSell());
        st.setInt(3, object.getOwner().getId());
        st.setDouble(4, object.getMaxCapacity());
        st.setBoolean(5, object.isAutoSell());
        int affectedRows = st.executeUpdate();

        if (affectedRows > 0) {
            final ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                object.setId(generatedKeys.getInt(1));
            }
        }

        st.close();
    }

    @Override
    public void create(ItemStorage object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("INSERT INTO tb_item_storage(max_capacity, auto_sell, owner_id) " +
                    "VALUES (?,?,?)");
            st.setDouble(1, object.getMaxCapacity());
            st.setBoolean(2, object.isAutoSell());
            st.setInt(3, object.getOwner().getId());

            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                final ResultSet generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                }
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemStorage readById(Integer object) {
        final ItemStorage itemStorage = new ItemStorage(object);
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("SELECT * FROM tb_item_storage WHERE id = ?");
            st.setInt(1, object);
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                itemStorage.setMaxCapacity(rs.getDouble("max_capacity"));
                itemStorage.setAutoSell(rs.getBoolean("auto_sell"));
                itemStorage.setOwner(new PlayerStorage(rs.getInt("owner_id")));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemStorage;
    }

    @Override
    public void update(ItemStorage object) {
        try (Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("UPDATE tb_item_storage " +
                    "SET max_capacity = ?, auto_sell = ? " +
                    "WHERE id = ?");
            st.setDouble(1, object.getMaxCapacity());
            st.setBoolean(2, object.isAutoSell());
            st.executeUpdate();
            st.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer object) {
        try (Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("DELETE FROM tb_item_storage WHERE id = ?");
            st.setInt(1, object);
            st.executeUpdate();
            st.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemStorage readByPlayerStorage(PlayerStorage playerStorage) {
        final ItemStorage itemStorage = new ItemStorage();
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("SELECT * FROM tb_item_storage WHERE owner_id = ?");
            st.setInt(1, playerStorage.getId());
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                itemStorage.setId(rs.getInt("id"));
                itemStorage.setMaxCapacity(rs.getDouble("max_capacity"));
                itemStorage.setAutoSell(rs.getBoolean("auto_sell"));
                itemStorage.setOwner(playerStorage);
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemStorage;
    }
}
