package com.blinkit.controller;

import com.blinkit.dao.UserDAO;
import com.blinkit.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;

@WebServlet({"/auth", "/adminAuth"})
public class AuthServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();


    // ==========================================
    // POST
    // ==========================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");

        System.out.println(
                "AUTH ACTION = " + action
        );


        // ==========================================
        // USER REGISTER
        // ==========================================

        if ("register".equals(action)) {

            String name =
                    request.getParameter("name");

            String email =
                    request.getParameter("email");

            String mobile =
                    request.getParameter("phone");

            String password =
                    request.getParameter("password");


            System.out.println(
                    "Register Name: " + name
            );

            System.out.println(
                    "Register Email: " + email
            );

            System.out.println(
                    "Register Mobile: " + mobile
            );


            String check =
                    userDAO.checkExistingUser(
                            email,
                            mobile
                    );


            if ("EMAIL_EXISTS".equals(check)) {

                response.getWriter().write(
                        "You are already registered with this email."
                );

                return;
            }


            if ("MOBILE_EXISTS".equals(check)) {

                response.getWriter().write(
                        "You are already registered with this mobile number."
                );

                return;
            }


            if ("ERROR".equals(check)) {

                response.getWriter().write(
                        "Database error"
                );

                return;
            }


            User user = new User(
                    0,
                    mobile,
                    email,
                    name,
                    password,
                    "USER"
            );


            boolean registered =
                    userDAO.registerUser(user);


            if (registered) {

                System.out.println(
                        "REGISTRATION SUCCESSFUL"
                );

                response.getWriter().write(
                        "Registration successful"
                );

            } else {

                response.getWriter().write(
                        "Registration failed"
                );
            }

            return;
        }



        // ==========================================
        // UPDATE PROFILE
        // ==========================================

        if ("updateProfile".equals(action)) {

            HttpSession session =
                    request.getSession(false);


            if (session == null) {

                response.getWriter().write(
                        "Please login"
                );

                return;
            }


            User user =
                    (User) session.getAttribute("user");


            if (user == null) {

                response.getWriter().write(
                        "Please login"
                );

                return;
            }


            String name =
                    request.getParameter("name");

            String mobile =
                    request.getParameter("mobile");


            boolean success =
                    userDAO.updateProfile(
                            user.getId(),
                            name,
                            mobile
                    );


            if (success) {

                user.setName(name);

                user.setMobile(mobile);

                session.setAttribute(
                        "user",
                        user
                );


                response.getWriter().write(
                        "Success"
                );

            } else {

                response.getWriter().write(
                        "Failed"
                );
            }

            return;
        }



        // ==========================================
        // UPDATE ACCOUNT
        // ==========================================

        if ("updateAccount".equals(action)) {

            HttpSession session =
                    request.getSession(false);


            if (session == null) {

                response.getWriter().write(
                        "Please login"
                );

                return;
            }


            User user =
                    (User) session.getAttribute("user");


            if (user == null) {

                response.getWriter().write(
                        "Please login"
                );

                return;
            }


            String name =
                    request.getParameter("name");

            String email =
                    request.getParameter("email");

            String mobile =
                    request.getParameter("mobile");


            boolean success =
                    userDAO.updateAccount(
                            user.getId(),
                            name,
                            email,
                            mobile
                    );


            if (success) {

                user.setName(name);

                user.setEmail(email);

                user.setMobile(mobile);


                session.setAttribute(
                        "user",
                        user
                );


                response.getWriter().write(
                        "Success"
                );

            } else {

                response.getWriter().write(
                        "Failed"
                );
            }

            return;
        }



        // ==========================================
        // ADMIN REGISTER
        // ==========================================

        if ("adminRegister".equals(action)) {

            String name =
                    request.getParameter("name");

            String email =
                    request.getParameter("email");

            String mobile =
                    request.getParameter("phone");

            String password =
                    request.getParameter("password");


            System.out.println(
                    "Admin Register Name: " + name
            );

            System.out.println(
                    "Admin Register Email: " + email
            );

            System.out.println(
                    "Admin Register Mobile: " + mobile
            );


            String check =
                    userDAO.checkExistingUser(
                            email,
                            mobile
                    );


            if ("EMAIL_EXISTS".equals(check)) {

                response.getWriter().write(
                        "This email is already registered."
                );

                return;
            }


            if ("MOBILE_EXISTS".equals(check)) {

                response.getWriter().write(
                        "This mobile number is already registered."
                );

                return;
            }


            if ("ERROR".equals(check)) {

                response.getWriter().write(
                        "Database error"
                );

                return;
            }


            User admin = new User(
                    0,
                    mobile,
                    email,
                    name,
                    password,
                    "ADMIN"
            );


            boolean registered =
                    userDAO.registerUser(admin);


            if (registered) {

                System.out.println(
                        "ADMIN REGISTRATION SUCCESSFUL"
                );

                response.getWriter().write(
                        "Admin registration successful"
                );

            } else {

                response.getWriter().write(
                        "Admin registration failed"
                );
            }

            return;
        }



        // ==========================================
        // USER LOGIN
        // ==========================================

        if ("login".equals(action)) {

            String email =
                    request.getParameter("email");

            String password =
                    request.getParameter("password");


            System.out.println(
                    "Login Email: " + email
            );


            User user =
                    userDAO.validateLogin(
                            email,
                            password
                    );


            if (user != null) {

                HttpSession session =
                        request.getSession();


                session.setAttribute(
                        "user",
                        user
                );


                System.out.println(
                        "LOGIN SUCCESSFUL: " + email
                );


                response.getWriter().write(
                        "Login successful"
                );

            } else {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );


                response.getWriter().write(
                        "Invalid email or password"
                );
            }

            return;
        }



        // ==========================================
        // ADMIN LOGIN
        // ==========================================

        if ("adminLogin".equals(action)) {

            String email =
                    request.getParameter("email");

            String password =
                    request.getParameter("password");


            System.out.println(
                    "Admin Login Email: " + email
            );


            User admin =
                    userDAO.validateAdminLogin(
                            email,
                            password
                    );


            if (admin != null) {

                HttpSession session =
                        request.getSession();


                session.setAttribute(
                        "user",
                        admin
                );


                System.out.println(
                        "ADMIN LOGIN SUCCESSFUL: " + email
                );


                response.getWriter().write(
                        "Admin login successful"
                );

            } else {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );


                response.getWriter().write(
                        "Invalid admin email or password"
                );
            }

            return;
        }



        // ==========================================
        // USER FORGOT PASSWORD
        // ==========================================

        if ("forgotPassword".equals(action)) {

            String email =
                    request.getParameter("email");

            String newPassword =
                    request.getParameter("newPassword");


            System.out.println(
                    "Forgot Password Email: " + email
            );


            User user =
                    userDAO.findByEmail(email);


            if (user == null) {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );


                response.getWriter().write(
                        "No account found with this email"
                );

                return;
            }


            boolean updated =
                    userDAO.updatePassword(
                            email,
                            newPassword
                    );


            if (updated) {

                System.out.println(
                        "PASSWORD UPDATED: " + email
                );


                response.getWriter().write(
                        "Password updated successfully"
                );

            } else {

                response.setStatus(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                );


                response.getWriter().write(
                        "Password update failed"
                );
            }

            return;
        }



        // ==========================================
        // ADMIN FORGOT PASSWORD
        // ==========================================

        if ("adminForgotPassword".equals(action)) {

            String email =
                    request.getParameter("email");

            String newPassword =
                    request.getParameter("newPassword");


            System.out.println(
                    "Admin Forgot Password Email: " + email
            );


            User admin =
                    userDAO.findByEmail(email);


            if (admin == null) {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );


                response.getWriter().write(
                        "No admin account found with this email"
                );

                return;
            }



            // ==========================================
            // CHECK ADMIN ROLE
            // ==========================================

            if (!"ADMIN".equalsIgnoreCase(
                    admin.getRole())) {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );


                response.getWriter().write(
                        "This email is not registered as admin"
                );

                return;
            }


            boolean updated =
                    userDAO.updatePassword(
                            email,
                            newPassword
                    );


            if (updated) {

                System.out.println(
                        "ADMIN PASSWORD UPDATED: " + email
                );


                response.getWriter().write(
                        "Admin password updated successfully"
                );

            } else {

                response.setStatus(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                );


                response.getWriter().write(
                        "Admin password update failed"
                );
            }

            return;
        }



        // ==========================================
        // INVALID ACTION
        // ==========================================

        response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
        );


        response.getWriter().write(
                "Invalid action"
        );
    }



    // ==========================================
    // GET
    // ==========================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        String action =
                request.getParameter("action");



        // ==========================================
        // LOGOUT
        // ==========================================

        if ("logout".equals(action)) {

            HttpSession session =
                    request.getSession(false);


            if (session != null) {

                session.invalidate();
            }


            response.sendRedirect(
                    "login.html"
            );

            return;
        }



        // ==========================================
        // GET PROFILE / ACCOUNT
        // ==========================================

        if ("profile".equals(action)) {

            HttpSession session =
                    request.getSession(false);


            if (session == null ||
                    session.getAttribute("user") == null) {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );

                response.getWriter().write(
                        "Please login"
                );

                return;
            }


            User user =
                    (User) session.getAttribute("user");


            response.setContentType(
                    "application/json"
            );

            response.setCharacterEncoding(
                    "UTF-8"
            );


            String name =
                    user.getName() == null
                    ? ""
                    : user.getName();

            String email =
                    user.getEmail() == null
                    ? ""
                    : user.getEmail();

            String mobile =
                    user.getMobile() == null
                    ? ""
                    : user.getMobile();


            // Basic JSON escaping
            name = escapeJson(name);
            email = escapeJson(email);
            mobile = escapeJson(mobile);


            response.getWriter().write(

                    "{"
                    + "\"id\":" + user.getId()
                    + ",\"name\":\"" + name + "\""
                    + ",\"email\":\"" + email + "\""
                    + ",\"mobile\":\"" + mobile + "\""
                    + "}"
            );

            return;
        }



        // ==========================================
        // DEFAULT
        // ==========================================

        response.sendRedirect(
                "login.html"
        );
    }



    // ==========================================
    // JSON ESCAPE
    // ==========================================

    private String escapeJson(String value) {

        if (value == null) {

            return "";
        }


        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}