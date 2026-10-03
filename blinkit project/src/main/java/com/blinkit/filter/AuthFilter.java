package com.blinkit.filter;

import com.blinkit.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import java.io.IOException;

@WebFilter({
    "//*",
    "/order/*",
    "/orders/*",
    "/admin/*",
    "/api//*",
    "/api/order/*",
    "/api/orders/*",
    "/api/admin/*"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain)
                         throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) req;

        HttpServletResponse response =
                (HttpServletResponse) res;

        HttpSession session =
                request.getSession(false);

        // Check whether user is logged in
        boolean loggedIn =
                session != null &&
                session.getAttribute("user") != null;

        // User is not logged in
        if (!loggedIn) {

            String uri = request.getRequestURI();

            // For API requests
            if (uri.contains("/api/")) {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(
                    "{\"success\":false,\"message\":\"Please login first\"}"
                );

            } else {

                // For normal pages
                response.sendRedirect(
                        request.getContextPath()
                        + "/login.html"
                );
            }

            return;
        }

        // Get logged-in user
        User user =
                (User) session.getAttribute("user");

        String uri =
                request.getRequestURI();

        // Admin-only area
        if (uri.contains("/admin/")
                || uri.contains("/api/admin/")) {

            if (user == null || !user.isAdmin()) {

                response.setStatus(
                        HttpServletResponse.SC_FORBIDDEN
                );

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(
                    "{\"success\":false,\"message\":\"Admins only\"}"
                );

                return;
            }
        }

        // Allow request
        chain.doFilter(request, response);
    }
}