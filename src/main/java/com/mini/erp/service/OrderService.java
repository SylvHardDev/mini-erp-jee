package com.mini.erp.service;

import com.mini.erp.dao.ClientDAO;
import com.mini.erp.dao.OrderDAO;
import com.mini.erp.dao.ProductDAO;
import com.mini.erp.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public List<Order> listAll() {
        return orderDAO.findAll();
    }

    public Order getById(Long id) {
        return orderDAO.findById(id);
    }

    public void create(Order order) {
        validate(order);
        orderDAO.save(order);
    }

    public void delete(Long id) {
        orderDAO.delete(id);
    }

    /**
     * Construit une commande à partir des paramètres du formulaire (clientId, productId[], quantity[]).
     * Les lignes avec quantité <= 0 ou produit invalide sont ignorées.
     */
    public Order buildOrderFromParams(Long clientId, Long[] productIds, Integer[] quantities) {
        if (clientId == null) {
            throw new IllegalArgumentException("Le client est obligatoire.");
        }
        Client client = clientDAO.findById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client introuvable.");
        }

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDate.now());
        order.setStatus("DRAFT");

        if (productIds == null || quantities == null || productIds.length != quantities.length) {
            return order;
        }

        List<OrderLine> lines = new ArrayList<>();
        for (int i = 0; i < productIds.length; i++) {
            if (productIds[i] == null || quantities[i] == null || quantities[i] <= 0) continue;
            Product product = productDAO.findById(productIds[i]);
            if (product == null) continue;

            OrderLine line = new OrderLine();
            line.setOrder(order);
            line.setProduct(product);
            line.setQuantity(quantities[i]);
            line.setUnitPrice(product.getPrixUnitaire() != null ? product.getPrixUnitaire() : BigDecimal.ZERO);
            lines.add(line);
            order.getLines().add(line);
        }
        return order;
    }

    private void validate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Commande invalide.");
        }
        if (order.getClient() == null) {
            throw new IllegalArgumentException("Le client est obligatoire.");
        }
        if (order.getOrderDate() == null) {
            throw new IllegalArgumentException("La date de commande est obligatoire.");
        }
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("DRAFT");
        }
        if (order.getLines() == null || order.getLines().isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins une ligne.");
        }
        for (OrderLine line : order.getLines()) {
            if (line.getQuantity() == null || line.getQuantity() <= 0) {
                throw new IllegalArgumentException("Chaque ligne doit avoir une quantité strictement positive.");
            }
            if (line.getProduct() == null) {
                throw new IllegalArgumentException("Chaque ligne doit référencer un produit.");
            }
        }
    }
}
