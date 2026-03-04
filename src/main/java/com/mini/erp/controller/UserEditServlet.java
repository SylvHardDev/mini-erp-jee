package com.mini.erp.controller;

import com.mini.erp.model.User;
import com.mini.erp.service.UserManagementService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/users/edit")
public class UserEditServlet extends HttpServlet {

    private final UserManagementService userService = new UserManagementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        User user = userService.getById(id);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        User existing = userService.getById(id);
        if (existing == null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }

        User user = new User();
        user.setId(id);
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setRole(request.getParameter("role"));

        try {
            userService.update(user);
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (IllegalArgumentException e) {
            request.setAttribute("user", existing);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/user-form.jsp").forward(request, response);
        }
    }

    private Long parseId(String idParam) {
        if (idParam == null) return null;
        try {
            return Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
