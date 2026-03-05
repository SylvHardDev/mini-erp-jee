package com.mini.erp.controller;

import com.mini.erp.model.Invoice;
import com.mini.erp.model.Order;
import com.mini.erp.service.InvoiceService;
import com.mini.erp.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> orders = orderService.listAll();
        request.setAttribute("orders", orders);

        Map<Long, Long> invoiceIdByOrderId = new HashMap<>();
        for (Invoice inv : invoiceService.listAll()) {
            if (inv.getOrder() != null) {
                invoiceIdByOrderId.put(inv.getOrder().getId(), inv.getId());
            }
        }
        request.setAttribute("invoiceIdByOrderId", invoiceIdByOrderId);

        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}
