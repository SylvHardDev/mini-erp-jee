package com.mini.erp.service;

import com.mini.erp.dao.InvoiceDAO;
import com.mini.erp.model.*;

import java.time.LocalDate;
import java.util.List;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final OrderService orderService = new OrderService();

    public List<Invoice> listAll() {
        return invoiceDAO.findAll();
    }

    public Invoice getById(Long id) {
        return invoiceDAO.findById(id);
    }

    /** Retourne la facture liée à la commande, ou null. */
    public Invoice getByOrderId(Long orderId) {
        return invoiceDAO.findByOrderId(orderId);
    }

    /**
     * Génère une facture à partir d'une commande : numéro séquentiel, snapshot client et lignes.
     * Une commande ne peut être facturée qu'une fois.
     */
    public Invoice createFromOrder(Long orderId) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Commande introuvable.");
        }
        if (invoiceDAO.findByOrderId(orderId) != null) {
            throw new IllegalArgumentException("Cette commande a déjà une facture.");
        }
        if (order.getLines() == null || order.getLines().isEmpty()) {
            throw new IllegalArgumentException("La commande ne contient aucune ligne.");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateNextInvoiceNumber());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setOrder(order);
        invoice.setStatus("DRAFT");

        Client c = order.getClient();
        invoice.setClientName(c != null && c.getName() != null ? c.getName() : "");
        invoice.setClientEmail(c != null ? c.getEmail() : null);
        if (c != null) {
            StringBuilder addr = new StringBuilder();
            if (c.getAddress() != null && !c.getAddress().isBlank()) addr.append(c.getAddress());
            if (c.getCity() != null && !c.getCity().isBlank()) {
                if (addr.length() > 0) addr.append(", ");
                addr.append(c.getCity());
            }
            if (c.getCountry() != null && !c.getCountry().isBlank()) {
                if (addr.length() > 0) addr.append(" ");
                addr.append(c.getCountry());
            }
            invoice.setClientAddress(addr.length() > 0 ? addr.toString() : null);
        }

        for (OrderLine ol : order.getLines()) {
            InvoiceLine line = new InvoiceLine();
            line.setInvoice(invoice);
            line.setProductReference(ol.getProduct() != null ? ol.getProduct().getReference() : null);
            line.setProductLabel(ol.getProduct() != null ? ol.getProduct().getLibelle() : "");
            line.setQuantity(ol.getQuantity());
            line.setUnitPrice(ol.getUnitPrice());
            invoice.getLines().add(line);
        }

        invoiceDAO.save(invoice);
        return invoice;
    }

    private String generateNextInvoiceNumber() {
        int year = LocalDate.now().getYear();
        String prefix = "FAC-" + year + "-";
        long count = invoiceDAO.countByNumberPrefix(prefix);
        return prefix + String.format("%04d", count + 1);
    }
}
