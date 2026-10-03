package com.blinkit.dao;

import com.blinkit.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {


    // =========================
    // GET ALL PRODUCTS
    // =========================

    public List<Product> getAllProducts() {

        List<Product> list = new ArrayList<>();

        String sql =
                "SELECT * FROM products ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(mapRow(rs));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return list;
    }


    // =========================
    // GET PRODUCTS BY CATEGORY
    // =========================

    public List<Product> getProductsByCategory(String category) {

        List<Product> list = new ArrayList<>();

        String sql =
                "SELECT * FROM products WHERE category = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return list;
    }


    // =========================
    // GET PRODUCT BY ID
    // =========================

    public Product getProductById(int id) {

        String sql =
                "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // =========================
    // ADD PRODUCT
    // =========================

    public boolean addProduct(Product p) {

        String sql =
                "INSERT INTO products " +
                "(name, quantity, price, image, category, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());

            ps.setString(2, p.getQuantity());

            ps.setBigDecimal(3, p.getPrice());

            ps.setString(4, p.getImage());

            ps.setString(5, p.getCategory());

            ps.setString(6, p.getDescription());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // UPDATE PRODUCT
    // =========================

    public boolean updateProduct(Product p) {

        String sql =
                "UPDATE products SET " +
                "name=?, quantity=?, price=?, image=?, " +
                "category=?, description=? " +
                "WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());

            ps.setString(2, p.getQuantity());

            ps.setBigDecimal(3, p.getPrice());

            ps.setString(4, p.getImage());

            ps.setString(5, p.getCategory());

            ps.setString(6, p.getDescription());

            ps.setInt(7, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // DELETE PRODUCT
    // =========================

    public boolean deleteProduct(int id) {

        String sql =
                "DELETE FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // REDUCE QUANTITY
    // =========================

    public boolean reduceStock(int productId, int quantity) {

        String sql =
                "UPDATE products " +
                "SET quantity = quantity - ? " +
                "WHERE id = ? AND quantity >= ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);

            ps.setInt(2, productId);

            ps.setInt(3, quantity);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // MAP DATABASE ROW
    // =========================

    private Product mapRow(ResultSet rs)
            throws SQLException {

        return new Product(

                rs.getInt("id"),

                rs.getString("name"),

                rs.getString("description"),

                rs.getBigDecimal("price"),

                rs.getString("category"),

                rs.getString("quantity"),

                rs.getString("image")
        );
    }
}