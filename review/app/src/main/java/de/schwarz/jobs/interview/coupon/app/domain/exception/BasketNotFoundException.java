package de.schwarz.jobs.interview.coupon.app.domain.exception;

public class BasketNotFoundException extends RuntimeException {

  public BasketNotFoundException(String message) {
    super(message);
  }
}
