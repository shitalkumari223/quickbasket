package com.blinkit.dao;

import com.blinkit.model.CartItem;
import com.blinkit.model.Order;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {


    // =================================================
    // PLACE ORDER
    // =================================================

    public int placeOrder(
            int userId,
            String address) {

        Connection conn = null;

        try {

            conn =
                    DBConnection.getConnection();

            if (conn == null) {

                System.out.println(
                        "DATABASE CONNECTION FAILED"
                );

                return -1;
            }


            conn.setAutoCommit(false);


            // =================================================
            // GET CART ITEMS
            // =================================================

            List<CartItem> cart =
                    new ArrayList<>();

            String cartSql =
                    "SELECT c.id, c.user_id, " +
                    "c.product_id, c.quantity, " +
                    "p.name, p.price " +
                    "FROM cart c " +
                    "JOIN products p " +
                    "ON c.product_id = p.id " +
                    "WHERE c.user_id = ?";


            try (
                PreparedStatement ps =
                        conn.prepareStatement(cartSql)
            ) {

                ps.setInt(1, userId);

                ResultSet rs =
                        ps.executeQuery();


                while (rs.next()) {

                    CartItem item =
                            new CartItem();

                    item.setId(
                            rs.getInt("id")
                    );

                    item.setUserId(
                            rs.getInt("user_id")
                    );

                    item.setProductId(
                            rs.getInt("product_id")
                    );

                    item.setName(
                            rs.getString("name")
                    );

                    item.setPrice(
                            rs.getBigDecimal("price")
                    );

                    item.setQuantity(
                            rs.getInt("quantity")
                    );

                    cart.add(item);
                }
            }


            // =================================================
            // EMPTY CART
            // =================================================

            if (cart.isEmpty()) {

                System.out.println(
                        "CART IS EMPTY"
                );

                conn.rollback();

                return -1;
            }


            // =================================================
            // CALCULATE TOTAL
            // =================================================

            BigDecimal total =
                    BigDecimal.ZERO;


            for (CartItem item : cart) {

                BigDecimal subtotal =
                        item.getPrice().multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        );

                total =
                        total.add(subtotal);
            }


            System.out.println(
                    "ORDER TOTAL = " + total
            );


            // =================================================
            // INSERT INTO ORDERS
            // =================================================

            String orderSql =
                    "INSERT INTO orders " +
                    "(user_id, total_amount, " +
                    "status, address) " +
                    "VALUES (?, ?, ?, ?)";


            int orderId;


            try (
                PreparedStatement ps =
                        conn.prepareStatement(
                                orderSql,
                                Statement.RETURN_GENERATED_KEYS
                        )
            ) {

                ps.setInt(1, userId);

                ps.setBigDecimal(
                        2,
                        total
                );

                ps.setString(
                        3,
                        "PLACED"
                );

                ps.setString(
                        4,
                        address
                );


                int rows =
                        ps.executeUpdate();


                if (rows == 0) {

                    conn.rollback();

                    return -1;
                }


                ResultSet rs =
                        ps.getGeneratedKeys();


                if (!rs.next()) {

                    conn.rollback();

                    return -1;
                }


                orderId =
                        rs.getInt(1);
            }


            System.out.println(
                    "ORDER ID = " + orderId
            );


            // =================================================
            // INSERT ORDER ITEMS
            // =================================================

            String itemSql =
                    "INSERT INTO order_items " +
                    "(order_id, product_id, " +
                    "quantity, price) " +
                    "VALUES (?, ?, ?, ?)";


            try (
                PreparedStatement ps =
                        conn.prepareStatement(itemSql)
            ) {

                for (CartItem item : cart) {

                    ps.setInt(
                            1,
                            orderId
                    );

                    ps.setInt(
                            2,
                            item.getProductId()
                    );

                    ps.setInt(
                            3,
                            item.getQuantity()
                    );

                    ps.setBigDecimal(
                            4,
                            item.getPrice()
                    );

                    ps.addBatch();
                }


                ps.executeBatch();
            }


            // =================================================
            // CLEAR CART
            // =================================================

            String clearSql =
                    "DELETE FROM cart " +
                    "WHERE user_id = ?";


            try (
                PreparedStatement ps =
                        conn.prepareStatement(
                                clearSql
                        )
            ) {

                ps.setInt(
                        1,
                        userId
                );

                ps.executeUpdate();
            }


            // =================================================
            // COMMIT
            // =================================================

            conn.commit();


            System.out.println(
                    "ORDER SAVED SUCCESSFULLY"
            );


            return orderId;


        } catch (Exception e) {

            e.printStackTrace();


            try {

                if (conn != null) {

                    conn.rollback();
                }

            } catch (SQLException ex) {

                ex.printStackTrace();
            }


            return -1;


        } finally {

            try {

                if (conn != null) {

                    conn.close();
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    // =================================================
    // GET MY ORDERS
    // =================================================

    public List<Order> getOrdersByUser(
            int userId) {

        List<Order> list =
                new ArrayList<>();


        String sql =
                "SELECT * FROM orders " +
                "WHERE user_id = ? " +
                "ORDER BY id DESC";


        try (
            Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    userId
            );


            ResultSet rs =
                    ps.executeQuery();


            while (rs.next()) {

                Order order =
                        new Order(

                            rs.getInt("id"),

                            rs.getInt("user_id"),

                            rs.getBigDecimal(
                                    "total_amount"
                            ),

                            rs.getString(
                                    "status"
                            ),

                            rs.getString(
                                    "address"
                            ),

                            rs.getTimestamp(
                                    "created_at"
                            )
                        );


                list.add(order);
            }


        } catch (SQLException e) {

            e.printStackTrace();
        }


        return list;
    }


    // =================================================
    // GET ALL ORDERS - ADMIN
    // =================================================

    public List<Order> getAllOrders() {

        List<Order> list =
                new ArrayList<>();


        String sql =
                "SELECT * FROM orders " +
                "ORDER BY id DESC";


        try (
            Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery()
        ) {

            while (rs.next()) {

                Order order =
                        new Order(

                            rs.getInt("id"),

                            rs.getInt("user_id"),

                            rs.getBigDecimal(
                                    "total_amount"
                            ),

                            rs.getString(
                                    "status"
                            ),

                            rs.getString(
                                    "address"
                            ),

                            rs.getTimestamp(
                                    "created_at"
                            )
                        );


                list.add(order);
            }


        } catch (SQLException e) {

            e.printStackTrace();
        }


        return list;
    }


    // =================================================
    // UPDATE ORDER STATUS
    // =================================================

    public boolean updateStatus(
            int orderId,
            String status) {

        String sql =
                "UPDATE orders " +
                "SET status = ? " +
                "WHERE id = ?";


        try (
            Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    status
            );

            ps.setInt(
                    2,
                    orderId
            );


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}