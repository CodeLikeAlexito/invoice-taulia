package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuyerRequestDto {
  @NotNull
  private String name;
}
