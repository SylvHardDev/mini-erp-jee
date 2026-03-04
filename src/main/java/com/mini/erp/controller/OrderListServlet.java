package com.mini.erp.controller;

import com.mini.erp.model.Order;
import com.mini.erp.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> orders = orderService.listAll();
        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}
