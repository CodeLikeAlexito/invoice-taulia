package com.taulia.invoice.exception;

import java.util.UUID;

public class InvoiceNotFoundException extends RuntimeException{
  public InvoiceNotFoundException(UUID invoiceId) {
    super("Invoice not found with ID: " + invoiceId);
  }
}
