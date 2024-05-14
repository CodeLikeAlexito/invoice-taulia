package com.taulia.invoice.service;

import com.taulia.invoice.dto.request.BuyerRequestDto;
import com.taulia.invoice.persistence.entity.Buyer;
import com.taulia.invoice.persistence.repository.BuyerRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuyerService {

  private final BuyerRepository buyerRepository;

  public Buyer create(BuyerRequestDto request) {
    return buyerRepository.save(Buyer.create(null, request.getName()));
  }

  public List<Buyer> show() {
    return this.buyerRepository.findAll();
  }
}
