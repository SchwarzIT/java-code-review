package schwarz.jobs.interview.coupon.core.exception;

/**
 * Thrown when a coupon code doesn't exist.
 */
public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(final String code) {
        super(String.format("Coupon '%s' not found", code));
    }

}
