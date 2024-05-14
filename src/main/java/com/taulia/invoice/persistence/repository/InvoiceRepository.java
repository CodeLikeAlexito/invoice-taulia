package com.taulia.invoice.persistence.repository;

import com.taulia.invoice.persistence.entity.Invoice;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
  @Query("SELECT i FROM Invoice i WHERE i.buyer.id = :buyerId ORDER BY i.buyer.id")
  Page<Invoice> findInvoicesByBuyerIdSortedByBuyer(UUID buyerId, Pageable pageable);

  @Query("SELECT i FROM Invoice i WHERE i.supplier.id = :supplierId ORDER BY i.supplier.id")
  Page<Invoice> findInvoicesBySupplierIdSortedBySupplier(UUID supplierId, Pageable pageable);

}
