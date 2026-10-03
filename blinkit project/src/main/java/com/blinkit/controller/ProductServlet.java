package com.blinkit.controller;

import com.blinkit.dao.ProductDAO;
import com.blinkit.model.Product;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet({"/product", "/admin/product"})
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    private final Gson gson = new Gson();


    // =========================
    // GET PRODUCTS
    // =========================

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String idParam =
                request.getParameter("id");

        String category =
                request.getParameter("category");

        response.setContentType("application/json");


        // Get product by ID
        if (idParam != null) {

            Product product =
                    productDAO.getProductById(
                            Integer.parseInt(idParam)
                    );

            response.getWriter().write(
                    gson.toJson(product)
            );

        }


        // Get products by category
        else if (category != null) {

            List<Product> products =
                    productDAO.getProductsByCategory(
                            category
                    );

            response.getWriter().write(
                    gson.toJson(products)
            );

        }


        // Get all products
        else {

            List<Product> products =
                    productDAO.getAllProducts();

            response.getWriter().write(
                    gson.toJson(products)
            );
        }
    }


    // =========================
    // ADD / UPDATE / DELETE
    // =========================

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String action =
                request.getParameter("action");

        response.setContentType("text/plain");


        // =========================
        // DELETE PRODUCT
        // =========================

        if ("delete".equals(action)) {

            int id =
                    Integer.parseInt(
                            request.getParameter("id")
                    );

            boolean deleted =
                    productDAO.deleteProduct(id);

            response.getWriter().write(
                    deleted
                            ? "Deleted"
                            : "Failed to delete"
            );

            return;
        }


        // =========================
        // ADD / UPDATE PRODUCT
        // =========================

        Product product = new Product();


        // Product Name
        product.setName(
                request.getParameter("name")
        );


        // Product Description
        product.setDescription(
                request.getParameter("description")
        );


        // Product Price
        product.setPrice(
                new BigDecimal(
                        request.getParameter("price")
                )
        );


        // Product Category
        product.setCategory(
                request.getParameter("category")
        );


     // Product Quantity
        product.setQuantity(
            request.getParameter("quantity")
        );


        // Product Image
        product.setImage(
                request.getParameter("image")
        );


        boolean success;


        // =========================
        // UPDATE PRODUCT
        // =========================

        if ("update".equals(action)) {

            product.setId(
                    Integer.parseInt(
                            request.getParameter("id")
                    )
            );

            success =
                    productDAO.updateProduct(product);
        }


        // =========================
        // ADD PRODUCT
        // =========================

        else {

            success =
                    productDAO.addProduct(product);
        }


        // =========================
        // RESPONSE
        // =========================

        response.getWriter().write(
                success
                        ? "Success"
                        : "Failed"
        );
    }
}