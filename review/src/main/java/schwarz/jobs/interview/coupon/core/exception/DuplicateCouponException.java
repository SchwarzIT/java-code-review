package schwarz.jobs.interview.coupon.core.exception;

public class DuplicateCouponException extends RuntimeException {
    public DuplicateCouponException(final String code) {
        super("Coupon with code '" + code + "' already exists");
    }
}
