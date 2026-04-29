package schwarz.jobs.interview.coupon.common.exception;

import lombok.Getter;

@Getter
public class CouponApplicationException extends RuntimeException {

    private final String couponCode;

    private CouponApplicationException(final String message, final String couponCode) {
        super(message);
        this.couponCode = couponCode;
    }

    public static CouponApplicationException complain(final String message, final String couponCode) {
        return new CouponApplicationException(message, couponCode);
    }
}
