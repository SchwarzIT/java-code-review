package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateCouponDTO {

    @NotNull(message = "{coupon.discount.required}")
    private BigDecimal discount;

    @NotBlank(message = "{coupon.code.required}")
    private String code;

    @NotNull(message = "{coupon.minimumbasket.required}")
    private BigDecimal minBasketValue;
}
