package com.leonardo.minecraft.itemsale.internal.repositories.base;

import com.leonardo.minecraft.core.Core;
import com.leonardo.minecraft.core.api.database.ConnectionProvider;
import com.leonardo.minecraft.itemsale.api.repositories.PlayerStorageRepository;
import com.leonardo.minecraft.itemsale.models.PlayerStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public abstract class PlayerStorageRepositoryImpl implements PlayerStorageRepository {

    private final ConnectionProvider connectionProvider;
    private Core core;

    public PlayerStorageRepositoryImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    @Override
    public Set<PlayerStorage> findAll(Map<String, String> whereSearch, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public Set<PlayerStorage> findAll(Map<String, String> whereSearch, Integer limit) {
        return null;
    }

    @Override
    public Set<PlayerStorage> findAll(Map<String, String> whereSearch) {
        return null;
    }

    @Override
    public Set<PlayerStorage> findAll() {
        return null;
    }

    @Override
    public void saveAll(Set<PlayerStorage> objects) {
        objects.forEach(this::save);
    }

    @Override
    public void create(PlayerStorage object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("INSERT INTO tb_player_storage(" +
                    "username," +
                    "sale_bonus," +
                    "loot_multiplier" +
                    ") VALUES (?,?,?)");
            st.setString(1, object.getUsername());
            st.setFloat(2, object.getSaleBonus());
            st.setFloat(3, object.getLootMultiplier());
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
    public PlayerStorage readById(Integer object) {
        final PlayerStorage ps = new PlayerStorage(object);
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("SELECT * FROM tb_player_storage WHERE id = ?");
            st.setInt(1, object);
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                ps.setId(rs.getInt("id"));
                ps.setUsername(rs.getString("username"));
                ps.setSaleBonus(rs.getFloat("sale_bonus"));
                ps.setLootMultiplier(rs.getFloat("loot_multiplier"));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ps;
    }

    @Override
    public void update(PlayerStorage object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("UPDATE tb_player_storage SET " +
                    "sale_bonus = ?," +
                    "loot_multiplier = ?" +
                    "WHERE id = ?");
            st.setFloat(1, object.getSaleBonus());
            st.setFloat(2, object.getLootMultiplier());
            st.setInt(3, object.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer object) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("DELETE FROM tb_player_storage WHERE id = ?");
            st.setFloat(1, object);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void saveTemplate(PlayerStorage object, PreparedStatement st) throws SQLException {
        st.setString(1, object.getUsername());
        st.setFloat(2, object.getLootMultiplier());
        st.setFloat(3, object.getSaleBonus());
        st.setFloat(4, object.getLootMultiplier());
        st.setFloat(5, object.getSaleBonus());
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
    public boolean existsUsername(String username) {
        boolean rs = false;
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("SELECT username FROM tb_player_storage WHERE username = ?");
            st.setString(1, username);
            rs = st.executeQuery().next();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public PlayerStorage readByUsername(String username) {
        final PlayerStorage ps = new PlayerStorage();
        ps.setUsername(username);
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("SELECT (id, sale_bonus, loot_multiplier) FROM tb_player_storage WHERE username = ?");
            st.setString(1, username);
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                ps.setId(rs.getInt("id"));
                ps.setSaleBonus(rs.getFloat("sale_bonus"));
                ps.setLootMultiplier(rs.getFloat("loot_multiplier"));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ps;
    }

    @Override
    public void deleteByUsername(String username) {
        try (final Connection con = this.getConnectionProvider().getConnection()) {
            final PreparedStatement st = con.prepareStatement("DELETE * FROM tb_player_storage WHERE username = ?");
            st.setString(1, username);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
