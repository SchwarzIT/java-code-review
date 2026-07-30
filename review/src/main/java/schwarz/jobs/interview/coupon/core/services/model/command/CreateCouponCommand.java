package schwarz.jobs.interview.coupon.core.services.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * The data needed to create a new coupon.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCouponCommand {

    @NotNull
    @Positive
    @DecimalMax("100")
    private BigDecimal discount;

    @NotBlank
    @Size(max = 250)
    private String code;

    @PositiveOrZero
    private BigDecimal minBasketValue;

}
