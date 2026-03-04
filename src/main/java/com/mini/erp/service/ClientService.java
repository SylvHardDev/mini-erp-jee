package com.mini.erp.service;

import com.mini.erp.dao.ClientDAO;
import com.mini.erp.model.Client;

import java.util.List;

public class ClientService {

    private final ClientDAO clientDAO = new ClientDAO();

    public List<Client> listAll() {
        return clientDAO.findAll();
    }

    public Client getById(Long id) {
        return clientDAO.findById(id);
    }

    public void create(Client client) {
        validate(client);
        clientDAO.save(client);
    }

    public void update(Client client) {
        validate(client);
        clientDAO.update(client);
    }

    public void delete(Long id) {
        clientDAO.delete(id);
    }

    /**
     * Validation métier simple pour un client.
     * Lève IllegalArgumentException en cas de données invalides.
     */
    private void validate(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client invalide.");
        }

        if (client.getName() == null || client.getName().isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire.");
        }

        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email du client est obligatoire.");
        }

        String email = client.getEmail().trim();
        // Validation très simple du format d'email (pédagogique)
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Le format de l'email est invalide.");
        }

        client.setName(client.getName().trim());
        client.setEmail(email);
    }
}

