package com.mini.erp.controller;

import com.mini.erp.model.User;
import com.mini.erp.service.UserManagementService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/users/delete")
public class UserDeleteServlet extends HttpServlet {

    private final UserManagementService userService = new UserManagementService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long currentUserId = getCurrentUserId(request);
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Long id = Long.parseLong(idParam);
                userService.delete(id, currentUserId);
            } catch (NumberFormatException ignored) {
            } catch (IllegalArgumentException e) {
                request.getSession().setAttribute("userDeleteError", e.getMessage());
            }
        }
        response.sendRedirect(request.getContextPath() + "/users");
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        User current = (User) request.getSession().getAttribute("user");
        return current != null ? current.getId() : null;
    }
}
