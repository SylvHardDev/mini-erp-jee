package com.mini.erp.controller;

import com.mini.erp.model.Invoice;
import com.mini.erp.service.InvoiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/invoices")
public class InvoiceListServlet extends HttpServlet {

    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Invoice> invoices = invoiceService.listAll();
        request.setAttribute("invoices", invoices);

        request.getRequestDispatcher("/invoices.jsp").forward(request, response);
    }
}
