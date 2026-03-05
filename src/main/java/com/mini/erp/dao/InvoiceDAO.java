package com.mini.erp.dao;

import com.mini.erp.config.JPAUtil;
import com.mini.erp.model.Invoice;
import jakarta.persistence.EntityManager;

import java.util.List;

public class InvoiceDAO {

    public List<Invoice> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.order LEFT JOIN FETCH i.lines ORDER BY i.invoiceDate DESC, i.id DESC",
                    Invoice.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Invoice findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM Invoice i LEFT JOIN FETCH i.lines WHERE i.id = :id",
                    Invoice.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    /** Retourne la facture associée à cette commande, ou null. */
    public Invoice findByOrderId(Long orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM Invoice i WHERE i.order.id = :orderId",
                    Invoice.class)
                    .setParameter("orderId", orderId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public void save(Invoice invoice) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(invoice);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /** Compte les factures dont le numéro commence par le préfixe donné (ex. FAC-2025-). */
    public long countByNumberPrefix(String prefix) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(i) FROM Invoice i WHERE i.invoiceNumber LIKE :prefix",
                    Long.class)
                    .setParameter("prefix", prefix + "%")
                    .getSingleResult();
            return count != null ? count : 0;
        } finally {
            em.close();
        }
    }
}
