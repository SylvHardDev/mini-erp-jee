package com.mini.erp.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Ferme l'EntityManagerFactory à l'arrêt de l'application.
 * Le premier compte admin est créé via l'interface /setup quand la base est vide.
 */
@WebListener
public class DataInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Plus de création automatique d'admin : on affiche l'interface /setup si la base est vide
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}
