package com.mini.erp.config;

import com.mini.erp.dao.UserDAO;
import com.mini.erp.model.User;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Au démarrage de l'application, crée un utilisateur par défaut (admin/admin)
 * si la table des utilisateurs est vide. Pratique pour le premier déploiement.
 */
@WebListener
public class DataInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserDAO userDAO = new UserDAO();
        if (userDAO.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setRole("ADMIN");
            userDAO.save(admin);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}
