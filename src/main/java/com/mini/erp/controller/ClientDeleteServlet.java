package com.mini.erp.controller;

import com.mini.erp.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/clients/delete")
public class ClientDeleteServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Long id = Long.parseLong(idParam);
                clientService.delete(id);
            } catch (NumberFormatException ignored) {
                // Id invalide -> on ignore simplement
            }
        }

        response.sendRedirect(request.getContextPath() + "/clients");
    }
}

