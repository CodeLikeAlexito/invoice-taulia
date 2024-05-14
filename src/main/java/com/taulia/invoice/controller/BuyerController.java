package com.taulia.invoice.controller;

import com.taulia.invoice.dto.request.BuyerRequestDto;
import com.taulia.invoice.persistence.entity.Buyer;
import com.taulia.invoice.service.BuyerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer")
@RequiredArgsConstructor
public class BuyerController {

  private final BuyerService buyerService;

  @PostMapping
  public ResponseEntity<Buyer> create(@RequestBody BuyerRequestDto request) {
    return ResponseEntity.ok(buyerService.create(request));
  }

  @GetMapping
  public ResponseEntity<List<Buyer>> all() {
    return ResponseEntity.ok(this.buyerService.show());
  }

}
