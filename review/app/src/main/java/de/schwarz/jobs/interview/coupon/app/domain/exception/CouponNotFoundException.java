package de.schwarz.jobs.interview.coupon.app.domain.exception;

public class CouponNotFoundException extends RuntimeException {

  public CouponNotFoundException(String message) {
    super(message);
  }
}
