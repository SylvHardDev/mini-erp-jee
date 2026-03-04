package com.mini.erp.service;

import com.mini.erp.dao.ProductDAO;
import com.mini.erp.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> listAll() {
        return productDAO.findAll();
    }

    public Product getById(Long id) {
        return productDAO.findById(id);
    }

    public void create(Product product) {
        validate(product);
        productDAO.save(product);
    }

    public void update(Product product) {
        validate(product);
        productDAO.update(product);
    }

    public void delete(Long id) {
        productDAO.delete(id);
    }

    /**
     * Validation métier pour un produit.
     * Lève IllegalArgumentException en cas de données invalides.
     */
    private void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produit invalide.");
        }

        if (product.getReference() == null || product.getReference().isBlank()) {
            throw new IllegalArgumentException("La référence est obligatoire.");
        }

        if (product.getLibelle() == null || product.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libellé est obligatoire.");
        }

        if (product.getPrixUnitaire() == null || product.getPrixUnitaire().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le prix unitaire doit être positif ou nul.");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("Le stock ne peut pas être négatif.");
        }

        product.setReference(product.getReference().trim());
        product.setLibelle(product.getLibelle().trim());
        if (product.getDescription() != null) {
            product.setDescription(product.getDescription().trim());
        }
    }
}
