package com.mini.erp.dao;

import com.mini.erp.config.JPAUtil;
import com.mini.erp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserDAO {

    public User findByUsername(String username) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class)
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void save(User user) {

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        em.persist(user);

        em.getTransaction().commit();
        em.close();
    }
}