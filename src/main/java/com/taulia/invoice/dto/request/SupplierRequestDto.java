package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SupplierRequestDto {
  @NotNull
  private String name;
}
