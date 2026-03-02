package com.mini.erp.service;

import com.mini.erp.dao.UserDAO;
import com.mini.erp.model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password) {

        User user = userDAO.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
}