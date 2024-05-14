package com.taulia.invoice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.taulia.invoice.dto.request.InvoiceItemRequestDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
//TODO implement logging
public class InvoiceService {

  private final InvoiceRepository invoiceRepository;
  private final BuyerRepository buyerRepository;
  private final SupplierRepository supplierRepository;
  public Invoice create(@RequestBody InvoiceRequestDto request) {
    Buyer buyer = buyerRepository.findById(UUID.fromString(request.getBuyerId()))
        .orElseThrow(() -> new BuyerNotFoundException(request.getBuyerId()));

    Supplier supplier = supplierRepository.findById(UUID.fromString(request.getSupplierId()))
        .orElseThrow(() -> new SupplierNotFoundException(request.getSupplierId()));

    List<InvoiceItem> invoiceItems = createInvoiceItems(request);

    Invoice invoice = Invoice.create(null, request.getInvoiceNumber(), buyer, supplier, invoiceItems,
        LocalDate.now(), request.getDueDate());

    for (InvoiceItem item : invoiceItems) {
      item.setInvoice(invoice);
    }

    return invoiceRepository.save(invoice);
  }

  private static List<InvoiceItem> createInvoiceItems(InvoiceRequestDto request) {
    List<InvoiceItem> invoiceItems = new ArrayList<>();

    if (!Objects.isNull(request.getItems())) {
      for (InvoiceItemRequestDto itemDto : request.getItems()) {
        InvoiceItem invoiceItem = InvoiceItem.create(
            null,
            itemDto.getDescription(),
            itemDto.getQuantity(),
            itemDto.getPrice()
        );
        invoiceItems.add(invoiceItem);
      }
    }
    return invoiceItems;
  }

  public List<Invoice> all() {
    return invoiceRepository.findAll();
  }

  public Invoice show(UUID id) {
    return invoiceRepository.findById(id)
        .orElseThrow(() -> new InvoiceNotFoundException(id));
  }

  public void delete(UUID invoiceId) {
    if (!invoiceRepository.existsById(invoiceId)) {
      throw new InvoiceNotFoundException(invoiceId);
    }

    invoiceRepository.deleteById(invoiceId);
  }

  public Invoice update(UUID invoiceId, JsonNode patch) {
    Invoice invoice = this.invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));

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
}
