package schwarz.jobs.interview.coupon.core.exception;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(final String code) {
        super("Coupon with code '" + code + "' not found");
    }
}
