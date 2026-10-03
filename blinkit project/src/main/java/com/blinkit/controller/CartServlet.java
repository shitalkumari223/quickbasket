package com.blinkit.controller;

import com.blinkit.dao.CartDAO;
import com.blinkit.model.CartItem;
import com.blinkit.model.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final Gson gson = new Gson();

    private Integer getLoggedInUserId(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return null;
        }

        return user.getId();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Integer userId = getLoggedInUserId(request);

        response.setContentType("application/json");

        if (userId == null) {
            response.getWriter().write("[]");
            return;
        }

        List<CartItem> items = cartDAO.getCartByUser(userId);
        response.getWriter().write(gson.toJson(items));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getLoggedInUserId(request);

        response.setContentType("text/plain");

        if (userId == null) {
            response.getWriter().write("Please login first");
            return;
        }

        String action = request.getParameter("action");
        boolean success;

        switch (action) {

            case "add":
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                success = cartDAO.addToCart(userId, productId, quantity);
                break;

            case "update":
                int itemId = Integer.parseInt(request.getParameter("itemId"));
                int newQty = Integer.parseInt(request.getParameter("quantity"));
                success = cartDAO.updateQuantity(itemId, newQty);
                break;

            case "remove":
                int removeId = Integer.parseInt(request.getParameter("itemId"));
                success = cartDAO.removeItem(removeId);
                break;

            case "clear":
                success = cartDAO.clearCart(userId);
                break;

            default:
                success = false;
        }

        response.getWriter().write(success ? "Success" : "Failed");
    }
}