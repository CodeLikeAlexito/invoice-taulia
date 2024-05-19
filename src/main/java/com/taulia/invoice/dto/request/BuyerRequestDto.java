package com.taulia.invoice.dto.request;

import jakarta.validation.constraints.NotNull;

public record BuyerRequestDto (@NotNull String name) {
}
