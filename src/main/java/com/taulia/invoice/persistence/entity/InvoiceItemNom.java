package com.taulia.invoice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items_nom")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Getter
public class InvoiceItemNom {
  @Id
  @GeneratedValue
  private final UUID id;
  @Column(name = "name")
  private final String name;

  @OneToOne
  private final InvoiceItem items;
}
