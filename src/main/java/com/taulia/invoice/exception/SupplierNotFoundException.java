package com.taulia.invoice.exception;

public class SupplierNotFoundException extends RuntimeException{
  public SupplierNotFoundException(String supplierId) {
    super("Supplier not found with ID: " + supplierId);
  }
}
