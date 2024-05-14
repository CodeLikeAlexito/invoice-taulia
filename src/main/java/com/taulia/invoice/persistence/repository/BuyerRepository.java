package com.taulia.invoice.persistence.repository;

import com.taulia.invoice.persistence.entity.Buyer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, UUID> {

}
