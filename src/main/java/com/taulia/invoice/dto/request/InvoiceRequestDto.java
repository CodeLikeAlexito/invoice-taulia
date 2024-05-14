package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class InvoiceRequestDto {
  @NotBlank
  private Long invoiceNumber;
  @NotNull
  private String buyerId;
  @NotNull
  private String supplierId;
  @NotNull
  private LocalDate dueDate;
  private List<InvoiceItemRequestDto> items;
}
