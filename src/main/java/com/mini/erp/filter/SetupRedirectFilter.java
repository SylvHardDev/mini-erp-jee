package com.mini.erp.filter;

import com.mini.erp.dao.UserDAO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Si la base des utilisateurs est vide, redirige vers /setup au lieu d'afficher la page de login.
 * S'applique aux requêtes vers la racine (/) et vers /login (ou login.jsp).
 */
@WebFilter(urlPatterns = {"/", "/login", "/login.jsp"})
public class SetupRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();
        boolean isRootOrLogin = "/".equals(path) || "".equals(path)
                || "/login".equals(path) || "/login.jsp".equals(path);
        if (isRootOrLogin && !new UserDAO().hasAnyUser()) {
            resp.sendRedirect(req.getContextPath() + "/setup");
            return;
        }

        chain.doFilter(request, response);
    }
}
