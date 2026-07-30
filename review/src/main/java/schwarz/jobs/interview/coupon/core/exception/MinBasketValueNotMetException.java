package schwarz.jobs.interview.coupon.core.exception;

import java.math.BigDecimal;

/**
 * Thrown when a basket doesn't reach the minimum value a coupon requires.
 */
public class MinBasketValueNotMetException extends RuntimeException {

    public MinBasketValueNotMetException(final String code, final BigDecimal basketValue, final BigDecimal minBasketValue) {
        super(String.format("Coupon '%s' requires a minimum basket value of %s, got %s", code, minBasketValue, basketValue));
    }

}
