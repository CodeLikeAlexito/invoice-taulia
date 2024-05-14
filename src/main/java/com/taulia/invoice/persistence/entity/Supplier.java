package com.taulia.invoice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Getter
public final class Supplier {

  @Id
  @GeneratedValue
  private final UUID id;

  @Column(name = "name", columnDefinition = "VARCHAR(255)")
  private final String name;

  private Supplier(UUID id, String name) {
    this.id = id;
    this.name = name;
  }
  public static Supplier create(UUID id, String name) {
    return new Supplier(id, name);
  }

}
