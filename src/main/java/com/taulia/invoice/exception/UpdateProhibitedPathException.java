package com.taulia.invoice.exception;

public class UpdateProhibitedPathException extends RuntimeException{

  public UpdateProhibitedPathException(String prohibitedPath) {
    super("Cannot patch invoice, prohibited path: " + prohibitedPath);
  }

}
