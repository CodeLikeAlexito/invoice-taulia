package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceItemRequestDto {
  @NotBlank
  private String description;

  @NotNull
  private Long quantity;

  @NotNull
  private BigDecimal price;
}
