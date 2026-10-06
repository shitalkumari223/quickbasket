package com.blinkit.controller;

import com.blinkit.dao.OrderDAO;
import com.blinkit.model.Order;
import com.blinkit.model.User;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO =
            new OrderDAO();

    private final Gson gson =
            new Gson();


    // =================================================
    // GET USER ID
    // =================================================

    private int getUserId(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            return -1;
        }


        User user =
                (User) session.getAttribute(
                        "user"
                );


        if (user == null) {

            return -1;
        }


        return user.getId();
    }


    // =================================================
    // GET = MY ORDERS / ADMIN ALL ORDERS
    // =================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {


        // =================================================
        // ADMIN = VIEW ALL ORDERS
        // =================================================

        String action =
                request.getParameter("action");


        if ("all".equals(action)) {

            HttpSession session =
                    request.getSession(false);


            if (session == null) {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );

                response.getWriter().write(
                        "Please login as admin"
                );

                return;
            }


            User user =
                    (User) session.getAttribute(
                            "user"
                    );


            if (user == null ||
                !user.isAdmin()) {

                response.setStatus(
                        HttpServletResponse.SC_FORBIDDEN
                );

                response.getWriter().write(
                        "Admin access required"
                );

                return;
            }


            // Get all orders
            List<Order> orders =
                    orderDAO.getAllOrders();


            response.setContentType(
                    "application/json"
            );


            response.getWriter().write(
                    gson.toJson(orders)
            );


            return;
        }


        // =================================================
        // USER = MY ORDERS
        // =================================================

        int userId =
                getUserId(request);


        if (userId == -1) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().write(
                    "Please login"
            );

            return;
        }


        List<Order> orders =
                orderDAO.getOrdersByUser(
                        userId
                );


        response.setContentType(
                "application/json"
        );


        response.getWriter().write(
                gson.toJson(orders)
        );
    }


    // =================================================
    // POST = PLACE ORDER
    // =================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {


        int userId =
                getUserId(request);


        if (userId == -1) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().write(
                    "Please login"
            );

            return;
        }


        String action =
                request.getParameter(
                        "action"
                );


        response.setContentType(
                "text/plain"
        );


        // =================================================
        // PLACE ORDER
        // =================================================

        if ("place".equals(action)) {

            String address =
                    request.getParameter(
                            "address"
                    );


            if (
                address == null ||
                address.trim().isEmpty()
            ) {

                response.getWriter().write(
                        "Address required"
                );

                return;
            }


            int orderId =
                    orderDAO.placeOrder(
                            userId,
                            address
                    );


            if (orderId > 0) {

                response.getWriter().write(
                        "Order placed: " +
                        orderId
                );

            } else {

                response.getWriter().write(
                        "Failed to place order"
                );
            }
        }
    }
}