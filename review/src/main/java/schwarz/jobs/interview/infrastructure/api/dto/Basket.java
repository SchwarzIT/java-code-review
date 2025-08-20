package schwarz.jobs.interview.infrastructure.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Basket {

    @NotNull
    private BigDecimal value;

    private BigDecimal appliedDiscount;

    private boolean applicationSuccessful;

    public void applyDiscount(final BigDecimal discount) {
        this.applicationSuccessful = false;
        this.appliedDiscount = discount;
    }

}
