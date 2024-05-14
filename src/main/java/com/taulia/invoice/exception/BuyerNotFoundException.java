package com.taulia.invoice.exception;

public class BuyerNotFoundException extends RuntimeException{
  public BuyerNotFoundException(String buyerId) {
    super("Buyer not found with ID: " + buyerId);
  }
}
