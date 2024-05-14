package com.taulia.invoice.controller;

import com.taulia.invoice.dto.request.SupplierRequestDto;
import com.taulia.invoice.persistence.entity.Supplier;
import com.taulia.invoice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {
  private final SupplierService supplierService;

  @PostMapping
  public ResponseEntity<Supplier> create(@RequestBody SupplierRequestDto request) {
    return ResponseEntity.ok(supplierService.create(request));
  }
}
