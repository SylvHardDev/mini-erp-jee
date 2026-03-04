package com.mini.erp.filter;

import com.mini.erp.config.Role;
import com.mini.erp.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtre d'authentification et de contrôle d'accès par rôle :
 * - vérifie qu'un utilisateur est présent en session, sinon redirige vers /login ;
 * - vérifie que le rôle autorise l'URL demandée (clients → ADMIN ou COMMERCIAL, produits → ADMIN ou STOCK).
 */
@WebFilter(urlPatterns = {
        "/dashboard.jsp",
        "/clients",
        "/clients.jsp",
        "/clients/new",
        "/clients/edit",
        "/clients/delete",
        "/client-form.jsp",
        "/products",
        "/products.jsp",
        "/products/new",
        "/products/edit",
        "/products/delete",
        "/product-form.jsp",
        "/product-image",
        "/users",
        "/users.jsp",
        "/users/new",
        "/users/edit",
        "/users/delete",
        "/user-form.jsp",
        "/orders",
        "/orders.jsp",
        "/orders/new",
        "/orders/delete",
        "/order-form.jsp"
})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        String path = httpRequest.getServletPath();
        String role = user.getRole() != null ? user.getRole() : "";

        if (path != null) {
            if (path.startsWith("/clients") || "/client-form.jsp".equals(path)) {
                if (!Role.canAccessClients(role)) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard.jsp?access=denied");
                    return;
                }
            }
            if (path.startsWith("/products") || "/product-form.jsp".equals(path) || "/product-image".equals(path)) {
                if (!Role.canAccessProducts(role)) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard.jsp?access=denied");
                    return;
                }
            }
            if (path.startsWith("/users") || "/user-form.jsp".equals(path)) {
                if (!Role.canAccessUserManagement(role)) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard.jsp?access=denied");
                    return;
                }
            }
            if (path.startsWith("/orders") || "/order-form.jsp".equals(path)) {
                if (!Role.canAccessOrders(role)) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard.jsp?access=denied");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Rien de spécial à détruire pour l'instant
    }
}

