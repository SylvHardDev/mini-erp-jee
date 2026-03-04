package com.mini.erp.dao;

import com.mini.erp.config.JPAUtil;
import com.mini.erp.model.Order;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderDAO {

    public List<Order> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.client LEFT JOIN FETCH o.lines ORDER BY o.orderDate DESC",
                    Order.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Order findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT o FROM Order o LEFT JOIN FETCH o.client LEFT JOIN FETCH o.lines l LEFT JOIN FETCH l.product WHERE o.id = :id",
                    Order.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public void save(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Order order = em.find(Order.class, id);
            if (order != null) {
                em.remove(order);
            }
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
