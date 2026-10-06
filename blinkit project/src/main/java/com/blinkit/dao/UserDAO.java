package com.blinkit.dao;

import com.blinkit.model.User;
import java.sql.*;

public class UserDAO {

    // ==========================================
    // LOGIN
    // ==========================================
    public User validateLogin(String email, String password) {

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

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

 // ==========================================
 // ADMIN LOGIN
 // ==========================================
 public User validateAdminLogin(String email, String password) {

     String sql =
             "SELECT * FROM users " +
             "WHERE email = ? AND password = ? AND role = 'ADMIN'";

     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {

         ps.setString(1, email);
         ps.setString(2, password);

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
    // ==========================================
    // CHECK EMAIL OR MOBILE
    // ==========================================
    public String checkExistingUser(String email, String mobile) {

        String sql =
                "SELECT email, mobile FROM users " +
                "WHERE email = ? OR mobile = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, mobile);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String existingEmail =
                            rs.getString("email");

                    String existingMobile =
                            rs.getString("mobile");

                    if (existingEmail != null &&
                        existingEmail.equalsIgnoreCase(email)) {

                        return "EMAIL_EXISTS";
                    }

                    if (existingMobile != null &&
                        existingMobile.equals(mobile)) {

                        return "MOBILE_EXISTS";
                    }
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return "ERROR";
        }

        return "AVAILABLE";
    }


    // ==========================================
    // REGISTER USER
    // ==========================================
    public boolean registerUser(User user) {

        String sql =
                "INSERT INTO users " +
                "(mobile, email, name, password, role) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getMobile());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getName());
            ps.setString(4, user.getPassword());

            ps.setString(
                    5,
                    user.getRole() == null
                            ? "USER"
                            : user.getRole()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ==========================================
    // FIND USER BY ID
    // ==========================================
    public User getUserById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

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


    // ==========================================
    // FIND USER BY EMAIL
    // ==========================================
    public User findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

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


    // ==========================================
    // FIND USER BY MOBILE
    // ==========================================
    public User findByMobile(String mobile) {

        String sql = "SELECT * FROM users WHERE mobile = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mobile);

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
    public boolean updateProfile(
            int userId,
            String name,
            String mobile) {

        String sql =
                "UPDATE users SET name = ?, mobile = ? WHERE id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateAccount(
            int userId,
            String name,
            String email,
            String mobile) {

        String sql =
            "UPDATE users SET name = ?, email = ?, mobile = ? WHERE id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, mobile);
            ps.setInt(4, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    } 
    // ==========================================
    // UPDATE PASSWORD
    // ==========================================
    public boolean updatePassword(String email, String newPassword) {

        String sql =
                "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ==========================================
    // MAP DATABASE ROW TO USER
    // ==========================================
    private User mapRow(ResultSet rs) throws SQLException {

        return new User(
                rs.getInt("id"),
                rs.getString("mobile"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}