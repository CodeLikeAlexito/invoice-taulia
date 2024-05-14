package com.taulia.invoice.persistence.repository;

import com.taulia.invoice.persistence.entity.Supplier;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

}
