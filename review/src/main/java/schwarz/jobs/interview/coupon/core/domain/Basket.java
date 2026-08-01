package schwarz.jobs.interview.coupon.core.domain;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

    @NotNull
    @PositiveOrZero
    private BigDecimal value;

    private BigDecimal appliedDiscount;

    private Boolean applicationSuccessful;

    public void applyDiscount(final BigDecimal discount) {
        if (discount == null) {
            throw new IllegalArgumentException("Discount cannot be null");
        }
        this.appliedDiscount = discount;
        this.value = value.subtract(discount);
        this.applicationSuccessful = true;
    }

}
