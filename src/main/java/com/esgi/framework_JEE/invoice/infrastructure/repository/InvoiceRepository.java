package com.esgi.framework_JEE.invoice.infrastructure.repository;

import com.esgi.framework_JEE.invoice.domain.Invoice;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Invoice getInvoiceById(int id);

    List<Invoice> getInvoicesByUser(User user);

    void deleteAllByUser(User user);
}
