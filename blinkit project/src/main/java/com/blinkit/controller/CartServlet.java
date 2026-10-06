package com.blinkit.controller;

import com.blinkit.dao.CartDAO;
import com.blinkit.model.CartItem;
import com.blinkit.model.User;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart/manage")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO =
            new CartDAO();

    private final Gson gson =
            new Gson();


    private int getUserId(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return -1;
        }

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            return -1;
        }

        return user.getId();
    }


    // ================= GET CART =================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        int userId =
                getUserId(request);

        if (userId == -1) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter()
                    .write("Please login");

            return;
        }

        List<CartItem> cart =
                cartDAO.getCartByUser(userId);

        response.setContentType(
                "application/json"
        );

        response.getWriter()
                .write(
                        gson.toJson(cart)
                );
    }


    // ================= ADD / UPDATE / REMOVE =================

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

            response.getWriter()
                    .write("Please login");

            return;
        }

        String action =
                request.getParameter(
                        "action"
                );

        boolean success = false;


        // ADD
        if ("add".equals(action)) {

            int productId =
                    Integer.parseInt(
                            request.getParameter(
                                    "productId"
                            )
                    );

            int quantity =
                    Integer.parseInt(
                            request.getParameter(
                                    "quantity"
                            )
                    );

            success =
                    cartDAO.addToCart(
                            userId,
                            productId,
                            quantity
                    );
        }


        // UPDATE
        else if ("update".equals(action)) {

            int cartItemId =
                    Integer.parseInt(
                            request.getParameter(
                                    "cartItemId"
                            )
                    );

            int quantity =
                    Integer.parseInt(
                            request.getParameter(
                                    "quantity"
                            )
                    );

            success =
                    cartDAO.updateQuantity(
                            cartItemId,
                            quantity
                    );
        }


        // REMOVE
        else if ("remove".equals(action)) {

            int cartItemId =
                    Integer.parseInt(
                            request.getParameter(
                                    "cartItemId"
                            )
                    );

            success =
                    cartDAO.removeItem(
                            cartItemId
                    );
        }


        // CLEAR
        else if ("clear".equals(action)) {

            success =
                    cartDAO.clearCart(
                            userId
                    );
        }


        response.setContentType(
                "text/plain"
        );

        response.getWriter().write(
                success
                        ? "Success"
                        : "Failed"
        );
    }
}