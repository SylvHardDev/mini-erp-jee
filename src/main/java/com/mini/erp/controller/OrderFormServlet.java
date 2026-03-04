package com.mini.erp.controller;

import com.mini.erp.model.Client;
import com.mini.erp.model.Order;
import com.mini.erp.model.Product;
import com.mini.erp.service.ClientService;
import com.mini.erp.service.OrderService;
import com.mini.erp.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders/new")
public class OrderFormServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final ClientService clientService = new ClientService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Client> clients = clientService.listAll();
        List<Product> products = productService.listAll();
        request.setAttribute("clients", clients);
        request.setAttribute("products", products);

        request.getRequestDispatcher("/order-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String clientIdParam = request.getParameter("clientId");
        String[] productIdParams = request.getParameterValues("productId");
        String[] quantityParams = request.getParameterValues("quantity");

        Long clientId = null;
        if (clientIdParam != null && !clientIdParam.isBlank()) {
            try {
                clientId = Long.parseLong(clientIdParam.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        int n = Math.min(productIdParams != null ? productIdParams.length : 0, quantityParams != null ? quantityParams.length : 0);
        Long[] productIds = new Long[n];
        Integer[] quantities = new Integer[n];
        for (int i = 0; i < n; i++) {
            try {
                productIds[i] = Long.parseLong(productIdParams[i].trim());
            } catch (Exception e) {
                productIds[i] = null;
            }
            try {
                quantities[i] = Integer.parseInt(quantityParams[i].trim());
            } catch (Exception e) {
                quantities[i] = 0;
            }
        }

        try {
            Order order = orderService.buildOrderFromParams(clientId, productIds, quantities);
            orderService.create(order);
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            List<Client> clients = clientService.listAll();
            List<Product> products = productService.listAll();
            request.setAttribute("clients", clients);
            request.setAttribute("products", products);
            request.getRequestDispatcher("/order-form.jsp").forward(request, response);
        }
    }
}
