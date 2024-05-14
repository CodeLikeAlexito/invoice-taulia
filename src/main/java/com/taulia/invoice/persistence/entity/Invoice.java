package com.taulia.invoice.persistence.entity;

import static com.taulia.invoice.InvoiceApplication.MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.JsonPatch;
import com.taulia.invoice.util.DateAudit;
import com.taulia.invoice.exception.UpdateProhibitedPathException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.query.SyntaxException;

@Entity
@Table(name = "invoices")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Getter
public final class Invoice extends DateAudit {
  @Id
  @GeneratedValue
  @Column(name = "ID")
  private final UUID id;

  @Column(name = "invoice_number", unique = true)
  private final Long invoiceNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", nullable = false)
  private final Buyer buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id", nullable = false)
  private final Supplier supplier;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<InvoiceItem> items;

  @Column(name = "invoice_date", nullable = false)
  private final LocalDate invoiceDate;

  @Column(name = "due_date", nullable = false)
  private final LocalDate dueDate;

  private Invoice(UUID id, Long invoiceNumber, Buyer buyer, Supplier supplier,
      List<InvoiceItem> items, LocalDate invoiceDate, LocalDate dueDate) {
    super();
    this.id = id;
    this.invoiceNumber = invoiceNumber;
    this.buyer = buyer;
    this.supplier = supplier;
    this.items = items;
    this.invoiceDate = invoiceDate;
    this.dueDate = dueDate;
  }

  public static Invoice create(UUID id, Long invoiceNumber, Buyer buyer, Supplier supplier,
      List<InvoiceItem> items, LocalDate invoiceDate, LocalDate dueDate) {
    return new Invoice(id, invoiceNumber, buyer, supplier, items, invoiceDate, dueDate);
  }

  public Invoice patch(JsonNode patch) {

    Set<String> prohibitedPaths
        = Set.of("/id", "/buyer_id", "/supplier_id", "/invoice_date");

    for (JsonNode operationNode : patch) {
      String path = operationNode.get("path")
          .asText();

      if (prohibitedPaths.contains(path)) {
        throw new UpdateProhibitedPathException(path);
      }
    }

    ObjectNode invoice = MAPPER.valueToTree(this);
    JsonNode patchedInvoice = JsonPatch.apply(patch, invoice);
    try {
      Invoice updatedInvoice = MAPPER.treeToValue(patchedInvoice, Invoice.class);
      for (InvoiceItem item : updatedInvoice.getItems()) {
        item.setInvoice(updatedInvoice);
      }
      return updatedInvoice;
    } catch (JsonProcessingException e) {
      throw new SyntaxException(e.getMessage());
    }
  }


}
