package com.mini.erp.controller;

import com.mini.erp.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Sert l'image d'un produit (stockée en BLOB).
 * GET /product-image?id=123
 */
@WebServlet("/product-image")
public class ProductImageServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        var product = productService.getById(id);
        if (product == null || product.getImageData() == null || product.getImageData().length == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = product.getImageContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = "image/jpeg";
        }
        response.setContentType(contentType);
        response.setContentLength(product.getImageData().length);
        response.setHeader("Cache-Control", "private, max-age=3600");
        response.getOutputStream().write(product.getImageData());
    }
}
