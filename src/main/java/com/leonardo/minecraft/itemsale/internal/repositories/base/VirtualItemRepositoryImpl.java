package com.leonardo.minecraft.itemsale.internal.repositories.base;

import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.itemsale.api.repositories.VirtualItemRepository;
import com.leonardo.minecraft.itemsale.models.ItemStorage;
import com.leonardo.minecraft.itemsale.models.VirtualItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class VirtualItemRepositoryImpl implements VirtualItemRepository {

    private final ConnectionProvider connectionProvider;

    public VirtualItemRepositoryImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    @Override
    public Set<VirtualItem> findAll(Map<String, String> whereSearch, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public Set<VirtualItem> findAll(Map<String, String> whereSearch, Integer limit) {
        return null;
    }

    @Override
    public Set<VirtualItem> findAll(Map<String, String> whereSearch) {
        return null;
    }

    @Override
    public Set<VirtualItem> findAll() {
        return null;
    }

    @Override
    public void saveAll(Set<VirtualItem> objects) {
        objects.forEach(this::save);
    }

    @Override
    public void save(VirtualItem object) {
        if (object.getId() != null && object.getId() != 0)
            update(object);
        else create(object);
    }

    @Override
    public void create(VirtualItem object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("INSERT INTO tb_virtual_item(factual_item_id, quantity, item_storage_id) " +
                    "VALUES (?,?,?)");
            st.setString(1, object.getFactualItemId());
            st.setDouble(2, object.getQuantity());
            st.setInt(3, object.getItemStorage().getId());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VirtualItem readById(Integer object) {
        final VirtualItem item = new VirtualItem(object);
        try (Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("SELECT * FROM tb_item_storage WHERE id = ?");
            st.setInt(1, object);
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                item.setQuantity(rs.getDouble("quantity"));
                item.setFactualItemId(rs.getString("factual_item_id"));
                item.setItemStorage(new ItemStorage(rs.getInt("item_storage_id")));
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return item;
    }

    @Override
    public void update(VirtualItem object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("UPDATE tb_item_storage " +
                    "SET quantity = ? WHERE id = ?");
            st.setDouble(1, object.getQuantity());
            st.setInt(2, object.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("DELETE FROM tb_item_storage WHERE id = ?");
            st.setDouble(1, object);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<VirtualItem> findAllByItemStorage(ItemStorage itemStorage) {
        final Set<VirtualItem> items = new HashSet<>();
        try (Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("SELECT * FROM tb_item_storage WHERE item_storage_id = ?");
            st.setInt(1, itemStorage.getId());
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final VirtualItem item = new VirtualItem(rs.getInt("id"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setFactualItemId(rs.getString("factual_item_id"));
                item.setItemStorage(itemStorage);
                items.add(item);
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}
