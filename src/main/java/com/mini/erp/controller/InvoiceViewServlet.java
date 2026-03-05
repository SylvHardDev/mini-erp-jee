package com.mini.erp.controller;

import com.mini.erp.model.Invoice;
import com.mini.erp.service.InvoiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/invoices/view")
public class InvoiceViewServlet extends HttpServlet {

    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/invoices");
            return;
        }

        try {
            Long id = Long.parseLong(idParam.trim());
            Invoice invoice = invoiceService.getById(id);
            if (invoice == null) {
                response.sendRedirect(request.getContextPath() + "/invoices");
                return;
            }
            request.setAttribute("invoice", invoice);
            request.getRequestDispatcher("/invoice-view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/invoices");
        }
    }
}
