package com.mini.erp.controller;

import com.mini.erp.model.Invoice;
import com.mini.erp.service.InvoiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/invoices/generate")
public class InvoiceGenerateServlet extends HttpServlet {

    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdParam.trim());
            Invoice invoice = invoiceService.createFromOrder(orderId);
            response.sendRedirect(request.getContextPath() + "/invoices/view?id=" + invoice.getId());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/orders?error=" +
                    java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
        }
    }
}
