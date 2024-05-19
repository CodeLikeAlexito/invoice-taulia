package com.taulia.invoice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.taulia.invoice.dto.request.InvoiceRequestDto;
import com.taulia.invoice.exception.BuyerNotFoundException;
import com.taulia.invoice.exception.InvoiceNotFoundException;
import com.taulia.invoice.exception.SupplierNotFoundException;
import com.taulia.invoice.persistence.entity.Buyer;
import com.taulia.invoice.persistence.entity.Invoice;
import com.taulia.invoice.persistence.entity.InvoiceItem;
import com.taulia.invoice.persistence.entity.Supplier;
import com.taulia.invoice.persistence.repository.BuyerRepository;
import com.taulia.invoice.persistence.repository.InvoiceRepository;
import com.taulia.invoice.persistence.repository.SupplierRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@Slf4j
@AllArgsConstructor
public class InvoiceService {

  private final InvoiceRepository invoiceRepository;
  private final BuyerRepository buyerRepository;
  private final SupplierRepository supplierRepository;
  public Invoice create(@RequestBody InvoiceRequestDto request) {
    log.info("Create new invoice with invoice number: {} for buyer: {} and supplier: {}",
        request.invoiceNumber(), request.buyerId(), request.supplierId());

    Buyer buyer = buyerRepository.findById(UUID.fromString(request.buyerId()))
        .orElseThrow(() -> new BuyerNotFoundException(request.buyerId()));

    Supplier supplier = supplierRepository.findById(UUID.fromString(request.supplierId()))
        .orElseThrow(() -> new SupplierNotFoundException(request.supplierId()));

    List<InvoiceItem> invoiceItems = createInvoiceItems(request);

    Invoice invoice = Invoice.create(null, request.invoiceNumber(), buyer, supplier, invoiceItems,
        LocalDate.now(), request.dueDate());

    invoiceItems.forEach(item -> item.setInvoice(invoice));

    return invoiceRepository.save(invoice);
  }

  public List<Invoice> all() {
    log.info("Invoice Service. Return all invoices from repository");
    return invoiceRepository.findAll();
  }

  public Invoice show(UUID id) {
    log.info("Invoice Service. Return invoice by invoice UUID: {}", id);
    return invoiceRepository.findById(id)
        .orElseThrow(() -> new InvoiceNotFoundException(id));
  }

  public void delete(UUID invoiceId) {
    if (!invoiceRepository.existsById(invoiceId)) {
      throw new InvoiceNotFoundException(invoiceId);
    }
    log.info("Invoice Service. Delete invoice with id: {}", invoiceId);
    invoiceRepository.deleteById(invoiceId);
  }

  public Invoice update(UUID invoiceId, JsonNode patch) {
    Invoice invoice = this.invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

    log.info("Invoice Service. Update invoice with id: {}", invoice);

    Invoice updateInvoice = invoice.patch((patch));

    return this.invoiceRepository.save(updateInvoice);
  }

  public Page<Invoice> showInvoicesByBuyer(UUID buyerId, int page, int size) {
    Pageable pageable = (Pageable) PageRequest.of(page, size);
    return this.invoiceRepository.findInvoicesByBuyerIdSortedByBuyer(buyerId, pageable);
  }

  public Page<Invoice> showInvoicesForSupplier(UUID supplierId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return invoiceRepository.findInvoicesBySupplierIdSortedBySupplier(supplierId, pageable);
  }

  private static List<InvoiceItem> createInvoiceItems(InvoiceRequestDto request) {
    if (Objects.isNull(request.items())) {
      return Collections.emptyList();
    }

    return request.items().stream()
        .map(itemDto -> InvoiceItem.create(
            null,
            itemDto.description(),
            itemDto.quantity(),
            itemDto.price()
        ))
        .collect(Collectors.toList());
  }
}
