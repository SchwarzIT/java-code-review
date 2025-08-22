package schwarz.jobs.interview.infrastructure.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class BasketRequestDTO {
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal value;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal appliedDiscount;

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getAppliedDiscount() {
        return appliedDiscount;
    }
}
