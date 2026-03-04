package com.mini.erp.controller;

import com.mini.erp.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/orders/delete")
public class OrderDeleteServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Long id = Long.parseLong(idParam);
                orderService.delete(id);
            } catch (NumberFormatException ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/orders");
    }
}
