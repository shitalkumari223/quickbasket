package com.blinkit.controller;

import com.blinkit.dao.CartDAO;
import com.blinkit.dao.OrderDAO;
import com.blinkit.model.CartItem;
import com.blinkit.model.Order;
import com.blinkit.model.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

// Protected by AuthFilter (URL pattern /order/*)
@SuppressWarnings("serial")
@WebServlet("/order/manage")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO CartDAO = new CartDAO();
    private final Gson gson = new Gson();

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");
    }

    // GET /order/manage -> order history for the logged-in user
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = getLoggedInUser(request);
        List<Order> orders = orderDAO.getOrdersByUser(user.getId());

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(orders));
    }

    // POST /order/manage -> place an order from the user's current 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        String deliveryAddress = request.getParameter("address");

        List<CartItem> Items = CartDAO.getCartByUser(user.getId());
        response.setContentType("text/plain");

        if (Items.isEmpty()) {
            response.getWriter().write(" is empty");
            return;
        }

        int orderId = orderDAO.placeOrder(user.getId(), Items, deliveryAddress);

        if (orderId != -1) {
            CartDAO.clearCart(user.getId());
            response.getWriter().write("Order placed. Order ID: " + orderId);
        } else {
            response.getWriter().write("Failed to place order");
        }
    }
}
