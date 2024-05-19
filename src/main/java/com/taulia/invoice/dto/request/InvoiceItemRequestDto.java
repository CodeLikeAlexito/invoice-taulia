package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record InvoiceItemRequestDto (
  @NotBlank String description,
  @NotNull Long quantity,
  @NotNull BigDecimal price
) {
}
