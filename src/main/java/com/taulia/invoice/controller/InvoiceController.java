package com.taulia.invoice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.taulia.invoice.dto.request.InvoiceRequestDto;
import com.taulia.invoice.persistence.entity.Invoice;
import com.taulia.invoice.service.InvoiceService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

  private final InvoiceService invoiceService;

  @PostMapping
  public ResponseEntity<Invoice> create(@RequestBody InvoiceRequestDto request){
    log.info("Invoice controller. Create new invoice endpoint.");
    log.info("Invoice dto: invoiceNumber: {}, buyerId: {}, supplierId: {}, dueDate: {}", request.invoiceNumber(),
        request.buyerId(), request.supplierId(), request.dueDate());
    return ResponseEntity.ok(invoiceService.create(request));
  }

  @GetMapping
  public ResponseEntity<List<Invoice>> all() {
    log.info("Invoice controller. Return all invoices");
    return ResponseEntity.ok(invoiceService.all());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Invoice> show(@PathVariable UUID id) {
    log.info("Invoice controller. Return invoice with id: {}", id);
    return ResponseEntity.ok(invoiceService.show(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable UUID id) {
    invoiceService.delete(id);
    log.info("Invoice controller. Delete invoice with id: {}", id);
    return ResponseEntity.ok("Invoice with ID " + id + " deleted successfully.");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Invoice> update(@PathVariable("id") UUID invoiceId,
      @RequestBody(required = false) JsonNode patch) {
    log.info("Invoice controller. Updated invoice with id: {}", invoiceId);
    return ResponseEntity.ok(this.invoiceService.update(invoiceId, patch));
  }

  @GetMapping("/buyer/{buyerId}")
  public Page<Invoice> showInvoicesForBuyer(@PathVariable UUID buyerId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "30") int size) {
    log.info("Invoice controller. Return all invoices for buyer: {}", buyerId);
    return this.invoiceService.showInvoicesByBuyer(buyerId, page, size);
  }

  @GetMapping("/supplier/{supplierId}")
  public Page<Invoice> showInvoicesForSupplier(@PathVariable UUID supplierId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "30") int size) {
    log.info("Invoice controller. Return all invoices for supplier: {}", supplierId);
    return this.invoiceService.showInvoicesForSupplier(supplierId, page, size);
  }
}
