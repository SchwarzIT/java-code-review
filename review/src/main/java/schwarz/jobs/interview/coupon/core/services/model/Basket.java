package schwarz.jobs.interview.coupon.core.services.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Basket {

    private BigDecimal value;
    private BigDecimal appliedDiscount;
    private boolean applicationSuccessful;

    public void applyDiscount(final BigDecimal discount) {
        this.appliedDiscount = discount;
        this.value = this.value.subtract(discount);
        this.applicationSuccessful = true;
    }
}
