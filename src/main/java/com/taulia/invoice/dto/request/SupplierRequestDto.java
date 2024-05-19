package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotNull;

public record SupplierRequestDto (
  @NotNull String name
) {
}
