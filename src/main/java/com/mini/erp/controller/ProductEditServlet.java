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

@WebServlet("/products/edit")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class ProductEditServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        Product product = productService.getById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/product-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = parseId(getPartValue(request, "id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        Product existing = productService.getById(id);
        if (existing == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        existing.setReference(getPartValue(request, "reference"));
        existing.setLibelle(getPartValue(request, "libelle"));
        existing.setDescription(getPartValue(request, "description"));

        String prixStr = getPartValue(request, "prixUnitaire");
        if (prixStr != null && !prixStr.isBlank()) {
            try {
                existing.setPrixUnitaire(new BigDecimal(prixStr.replace(",", ".")));
            } catch (NumberFormatException ignored) {
            }
        }

        String stockStr = getPartValue(request, "stock");
        if (stockStr != null && !stockStr.isBlank()) {
            try {
                existing.setStock(Integer.parseInt(stockStr.trim()));
            } catch (NumberFormatException ignored) {
            }
        }

        Part imagePart = request.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0 && imagePart.getSubmittedFileName() != null && !imagePart.getSubmittedFileName().isBlank()) {
            existing.setImageData(imagePart.getInputStream().readAllBytes());
            String contentType = imagePart.getContentType();
            existing.setImageContentType(contentType != null && contentType.length() <= 100 ? contentType : "image/jpeg");
        }

        try {
            productService.update(existing);
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (IllegalArgumentException e) {
            request.setAttribute("product", existing);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/product-form.jsp").forward(request, response);
        }
    }

    private static String getPartValue(HttpServletRequest request, String partName) throws IOException, ServletException {
        Part part = request.getPart(partName);
        if (part == null) return request.getParameter(partName);
        try (InputStream is = part.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
    }

    private Long parseId(String idParam) {
        if (idParam == null) return null;
        try {
            return Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
