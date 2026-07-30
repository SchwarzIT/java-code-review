package schwarz.jobs.interview.coupon.core.services.model.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A shopping basket, with or without a coupon applied.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketDomain {

    @NotNull
    @PositiveOrZero
    private BigDecimal value;

    private BigDecimal appliedDiscount;

    private BigDecimal finalValue;

    private boolean applicationSuccessful;

    /**
     * Applies a percentage discount to this basket, working out {@link #finalValue}.
     */
    public void applyDiscount(final BigDecimal discountPercentage) {
        this.applicationSuccessful = true;
        this.appliedDiscount = discountPercentage;

        final BigDecimal discountAmount = value.multiply(discountPercentage)
            .divide(BigDecimal.valueOf(100));
        this.finalValue = value.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

}
