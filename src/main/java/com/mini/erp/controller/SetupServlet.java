package com.mini.erp.controller;

import com.mini.erp.dao.UserDAO;
import com.mini.erp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Affiche le formulaire de création du premier administrateur lorsque la base users est vide.
 * Une fois un utilisateur créé, redirige vers la page de login.
 */
@WebServlet("/setup")
public class SetupServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (userDAO.hasAnyUser()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.getRequestDispatcher("/setup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (userDAO.hasAnyUser()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || username.isBlank()) {
            request.setAttribute("error", "L'identifiant est obligatoire.");
            request.getRequestDispatcher("/setup.jsp").forward(request, response);
            return;
        }
        if (password == null || password.isBlank()) {
            request.setAttribute("error", "Le mot de passe est obligatoire.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/setup.jsp").forward(request, response);
            return;
        }
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Les deux mots de passe ne correspondent pas.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/setup.jsp").forward(request, response);
            return;
        }

        User admin = new User();
        admin.setUsername(username.trim());
        admin.setPassword(password);
        admin.setRole("ADMIN");
        userDAO.save(admin);

        response.sendRedirect(request.getContextPath() + "/login?created=1");
    }
}
