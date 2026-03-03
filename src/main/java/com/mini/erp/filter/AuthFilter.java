package com.mini.erp.filter;

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
 * Filtre d'authentification :
 * - vérifie qu'un utilisateur est présent en session
 * - sinon, redirige vers la page de login.
 *
 * Pour l'instant il protège uniquement dashboard.jsp.
 * On étendra ensuite aux autres pages métier (clients, produits, etc.).
 */
@WebFilter(urlPatterns = {"/dashboard.jsp"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // Rien de spécial à initialiser pour l'instant
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
            // Pas connecté → redirection vers la page de login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Utilisateur présent → on continue la chaîne normale
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Rien de spécial à détruire pour l'instant
    }
}

