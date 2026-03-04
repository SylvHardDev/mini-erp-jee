package com.mini.erp.controller;

import com.mini.erp.model.Client;
import com.mini.erp.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/clients")
public class ClientListServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Client> clients = clientService.listAll();
        request.setAttribute("clients", clients);

        request.getRequestDispatcher("/clients.jsp").forward(request, response);
    }
}

