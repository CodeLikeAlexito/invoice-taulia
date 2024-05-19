package com.taulia.invoice.service;

import com.taulia.invoice.dto.request.SupplierRequestDto;
import com.taulia.invoice.persistence.entity.Supplier;
import com.taulia.invoice.persistence.repository.SupplierRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupplierService {
  private final SupplierRepository supplierRepository;

  public Supplier create(SupplierRequestDto request) {
    return supplierRepository.save(Supplier.create(null, request.name()));
  }

  public List<Supplier> show() {
    return this.supplierRepository.findAll();
  }
}
