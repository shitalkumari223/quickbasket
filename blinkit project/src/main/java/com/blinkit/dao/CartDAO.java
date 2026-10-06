package com.blinkit.dao;

import com.blinkit.model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {


    // ================= ADD TO CART =================

    public boolean addToCart(
            int userId,
            int productId,
            int quantity) {

        String checkSql =
                "SELECT id, quantity " +
                "FROM cart " +
                "WHERE user_id = ? " +
                "AND product_id = ?";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                checkSql
                        )
        ) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                // Product already in cart
                if (rs.next()) {

                    int cartId =
                            rs.getInt("id");

                    int oldQuantity =
                            rs.getInt("quantity");

                    int newQuantity =
                            oldQuantity + quantity;

                    return updateQuantity(
                            cartId,
                            newQuantity
                    );
                }

                // New product
                String insertSql =
                        "INSERT INTO cart " +
                        "(user_id, product_id, quantity) " +
                        "VALUES (?, ?, ?)";

                try (
                        PreparedStatement insertPs =
                                conn.prepareStatement(
                                        insertSql
                                )
                ) {

                    insertPs.setInt(
                            1,
                            userId
                    );

                    insertPs.setInt(
                            2,
                            productId
                    );

                    insertPs.setInt(
                            3,
                            quantity
                    );

                    return
                            insertPs.executeUpdate()
                            > 0;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ================= GET CART =================

    public List<CartItem> getCartByUser(
            int userId) {

        List<CartItem> list =
                new ArrayList<>();

        String sql =
                "SELECT " +
                "c.id, " +
                "c.user_id, " +
                "c.product_id, " +
                "c.quantity, " +
                "p.name, " +
                "p.price, " +
                "p.image " +

                "FROM cart c " +

                "JOIN products p " +
                "ON c.product_id = p.id " +

                "WHERE c.user_id = ?";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    userId
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    CartItem item =
                            new CartItem(

                                    rs.getInt(
                                            "id"
                                    ),

                                    rs.getInt(
                                            "user_id"
                                    ),

                                    rs.getInt(
                                            "product_id"
                                    ),

                                    rs.getString(
                                            "name"
                                    ),

                                    rs.getBigDecimal(
                                            "price"
                                    ),

                                    rs.getString(
                                            "image"
                                    ),

                                    rs.getInt(
                                            "quantity"
                                    )
                            );

                    list.add(item);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return list;
    }


    // ================= UPDATE =================

    public boolean updateQuantity(
            int cartItemId,
            int quantity) {

        if (quantity <= 0) {

            return removeItem(
                    cartItemId
            );
        }

        String sql =
                "UPDATE cart " +
                "SET quantity = ? " +
                "WHERE id = ?";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    quantity
            );

            ps.setInt(
                    2,
                    cartItemId
            );

            return
                    ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ================= REMOVE =================

    public boolean removeItem(
            int cartItemId) {

        String sql =
                "DELETE FROM cart " +
                "WHERE id = ?";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    cartItemId
            );

            return
                    ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ================= CLEAR CART =================

    public boolean clearCart(
            int userId) {

        String sql =
                "DELETE FROM cart " +
                "WHERE user_id = ?";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            ps.setInt(
                    1,
                    userId
            );

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}