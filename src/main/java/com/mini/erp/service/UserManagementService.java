package com.mini.erp.service;

import com.mini.erp.config.Role;
import com.mini.erp.dao.UserDAO;
import com.mini.erp.model.User;

import java.util.List;

/**
 * Gestion des utilisateurs (employés) par l'admin : liste, création, édition, suppression.
 */
public class UserManagementService {

    private final UserDAO userDAO = new UserDAO();

    public List<User> listAll() {
        return userDAO.findAll();
    }

    public User getById(Long id) {
        return userDAO.findById(id);
    }

    public void create(User user) {
        validate(user);
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Un utilisateur avec cet identifiant existe déjà.");
        }
        userDAO.save(user);
    }

    public void update(User user) {
        validate(user);
        User existing = userDAO.findById(user.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Utilisateur introuvable.");
        }
        if (!existing.getUsername().equals(user.getUsername()) && userDAO.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Un utilisateur avec cet identifiant existe déjà.");
        }
        existing.setUsername(user.getUsername());
        existing.setRole(user.getRole());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(user.getPassword());
        }
        userDAO.update(existing);
    }

    public void delete(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new IllegalArgumentException("Vous ne pouvez pas supprimer votre propre compte.");
        }
        User user = userDAO.findById(id);
        if (user == null) {
            return;
        }
        userDAO.delete(id);
    }

    private void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur invalide.");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("L'identifiant est obligatoire.");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            throw new IllegalArgumentException("Le rôle est obligatoire.");
        }
        String role = user.getRole().toUpperCase();
        if (!Role.ADMIN.equals(role) && !Role.STOCK.equals(role) && !Role.COMMERCIAL.equals(role)) {
            throw new IllegalArgumentException("Rôle invalide. Valeurs autorisées : ADMIN, STOCK, COMMERCIAL.");
        }
        user.setUsername(user.getUsername().trim());
        user.setRole(role);
    }
}
