package com.taulia.invoice.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Getter
public final class InvoiceItem {
  @Id
  @GeneratedValue
  private final UUID id;

  @OneToOne
  @JoinColumn(name = "invoice_item_nom_id", nullable = false)
  @JsonIgnore
  private InvoiceItemNom nameId;

  private final Long quantity;
  private final BigDecimal amount;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "invoice_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  @Setter
  private Invoice invoice;

  private InvoiceItem(UUID id, Long quantity, BigDecimal amount) {
    this.id = id;
    this.quantity = quantity;
    this.amount = amount;
  }

  private InvoiceItem(UUID id, Long quantity, BigDecimal amount, Invoice invoice) {
    this.id = id;
    this.quantity = quantity;
    this.amount = amount;
    this.invoice = invoice;
  }

  public static InvoiceItem create(UUID id, Long quantity, BigDecimal amount) {
    return new InvoiceItem(id, quantity, amount);
  }
}
