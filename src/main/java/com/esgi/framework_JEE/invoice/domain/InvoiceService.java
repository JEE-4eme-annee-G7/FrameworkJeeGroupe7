package com.esgi.framework_JEE.invoice.domain;

import com.esgi.framework_JEE.basket.domain.Basket;
import com.esgi.framework_JEE.invoice.infrastructure.repository.InvoiceRepository;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice createEmpty(){
        var invoice = new Invoice()
                .setAmount(0.0)
                .setCreationDate(new Date());
        invoiceRepository.save(invoice);

        return invoice;
    }

    public Invoice generateWithUser(User user, Basket basket){
        var invoice = new Invoice()
                .setAmount(basket.getAmount())
                .setCreationDate(new Date())
                .setUser(user);

        invoiceRepository.save(invoice);
        return invoice;
    }

    public Invoice getById(int id){
        return invoiceRepository.getInvoiceById(id);
    }

    public List<Invoice> getByUser(User user){
        return invoiceRepository.getInvoicesByUser(user);
    }

    public List<Invoice> getAll(){
        return invoiceRepository.findAll();
    }

    public void deleteById(int id){
        var invoice = getById(id);
        invoice.setUser(null);
        invoiceRepository.deleteById(id);
    }

    public void deleteByUser(User user){
        var invoices = getByUser(user);
        invoices.forEach(invoice -> {
            invoice.setUser(null);
            invoiceRepository.save(invoice);
            invoiceRepository.delete(invoice);
        });
    }
}
