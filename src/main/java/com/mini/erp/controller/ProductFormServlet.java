package com.mini.erp.controller;

import com.mini.erp.model.Product;
import com.mini.erp.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@WebServlet("/products/new")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class ProductFormServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/product-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = buildProductFromRequest(request);

        try {
            productService.create(product);
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/product-form.jsp").forward(request, response);
        }
    }

    private Product buildProductFromRequest(HttpServletRequest request) throws IOException, ServletException {
        Product product = new Product();
        product.setReference(getPartValue(request, "reference"));
        product.setLibelle(getPartValue(request, "libelle"));
        product.setDescription(getPartValue(request, "description"));

        String prixStr = getPartValue(request, "prixUnitaire");
        if (prixStr != null && !prixStr.isBlank()) {
            try {
                product.setPrixUnitaire(new BigDecimal(prixStr.replace(",", ".")));
            } catch (NumberFormatException e) {
                product.setPrixUnitaire(BigDecimal.ZERO);
            }
        } else {
            product.setPrixUnitaire(BigDecimal.ZERO);
        }

        String stockStr = getPartValue(request, "stock");
        if (stockStr != null && !stockStr.isBlank()) {
            try {
                product.setStock(Integer.parseInt(stockStr.trim()));
            } catch (NumberFormatException e) {
                product.setStock(0);
            }
        } else {
            product.setStock(0);
        }

        Part imagePart = request.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0 && imagePart.getSubmittedFileName() != null && !imagePart.getSubmittedFileName().isBlank()) {
            product.setImageData(imagePart.getInputStream().readAllBytes());
            String contentType = imagePart.getContentType();
            if (contentType != null && contentType.length() <= 100) {
                product.setImageContentType(contentType);
            } else {
                product.setImageContentType("image/jpeg");
            }
        }

        return product;
    }

    private static String getPartValue(HttpServletRequest request, String partName) throws IOException, ServletException {
        Part part = request.getPart(partName);
        if (part == null) return request.getParameter(partName);
        try (InputStream is = part.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
    }
}
