package com.mini.erp.config;

/**
 * Rôles utilisateur et règles d'accès.
 * ADMIN : tout ; STOCK : produits uniquement ; COMMERCIAL : clients uniquement.
 */
public final class Role {

    public static final String ADMIN = "ADMIN";
    public static final String STOCK = "STOCK";
    public static final String COMMERCIAL = "COMMERCIAL";

    private Role() {
    }

    /** Accès à la gestion clients (liste, création, édition, suppression). */
    public static boolean canAccessClients(String role) {
        return ADMIN.equals(role) || COMMERCIAL.equals(role);
    }

    /** Accès à la gestion produits (liste, création, édition, suppression, images). */
    public static boolean canAccessProducts(String role) {
        return ADMIN.equals(role) || STOCK.equals(role);
    }

    /** Accès à la gestion des employés (utilisateurs) : réservé à l'admin. */
    public static boolean canAccessUserManagement(String role) {
        return ADMIN.equals(role);
    }

    /** Accès à la gestion des commandes : admin et commercial. */
    public static boolean canAccessOrders(String role) {
        return ADMIN.equals(role) || COMMERCIAL.equals(role);
    }

    /** Accès à la gestion des factures (liste, génération, vue) : admin et commercial. */
    public static boolean canAccessInvoices(String role) {
        return ADMIN.equals(role) || COMMERCIAL.equals(role);
    }
}
