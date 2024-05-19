package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record InvoiceRequestDto (
  @NotBlank Long invoiceNumber,
  @NotNull String buyerId,
  @NotNull String supplierId,
  @NotNull LocalDate dueDate,
  List<InvoiceItemRequestDto> items
) {
}
