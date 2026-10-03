package com.blinkit.controller;

import com.blinkit.dao.OrderDAO;
import com.blinkit.model.Order;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

// Protected by AuthFilter (URL pattern /admin/*), and AuthFilter also checks role == ADMIN
@WebServlet("/admin/orders")
public class AdminServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final Gson gson = new Gson();

    // GET /admin/orders -> list every order, for the admin dashboard
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        List<Order> orders = orderDAO.getAllOrders();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(orders));
    }

    // POST /admin/orders -> update an order's status (PACKED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String newStatus = request.getParameter("status");

        boolean success = orderDAO.updateStatus(orderId, newStatus);

        response.setContentType("text/plain");
        response.getWriter().write(success ? "Status updated" : "Failed to update");
    }
}
