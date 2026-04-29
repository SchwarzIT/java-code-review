package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BasketDTO {

    @NotNull(message = "{basket.value.required}")
    @DecimalMin(value = "0.0", message = "{basket.value.invalid}")
    private BigDecimal value;

    private BigDecimal appliedDiscount;
}
