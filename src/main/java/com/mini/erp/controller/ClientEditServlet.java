package com.mini.erp.controller;

import com.mini.erp.model.Client;
import com.mini.erp.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/clients/edit")
public class ClientEditServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/clients");
            return;
        }

        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/clients");
            return;
        }

        Client client = clientService.getById(id);
        if (client == null) {
            response.sendRedirect(request.getContextPath() + "/clients");
            return;
        }

        request.setAttribute("client", client);
        request.getRequestDispatcher("/client-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/clients");
            return;
        }

        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/clients");
            return;
        }

        Client existing = clientService.getById(id);
        if (existing == null) {
            response.sendRedirect(request.getContextPath() + "/clients");
            return;
        }

        existing.setName(request.getParameter("name"));
        existing.setEmail(request.getParameter("email"));
        existing.setPhone(request.getParameter("phone"));
        existing.setAddress(request.getParameter("address"));
        existing.setCity(request.getParameter("city"));
        existing.setCountry(request.getParameter("country"));

        try {
            clientService.update(existing);
            response.sendRedirect(request.getContextPath() + "/clients");
        } catch (IllegalArgumentException e) {
            request.setAttribute("client", existing);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/client-form.jsp").forward(request, response);
        }
    }
}

