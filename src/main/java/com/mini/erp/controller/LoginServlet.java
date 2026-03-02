package com.mini.erp.controller;

import com.mini.erp.model.User;
import com.mini.erp.service.AuthService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = authService.authenticate(username, password);

        if (user != null) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("dashboard.jsp");
        } else {
            request.setAttribute("error", "Identifiants invalides");
            request.getRequestDispatcher("login.jsp")
                    .forward(request, response);
        }
    }
}