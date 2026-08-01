package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    @NotNull
    @Positive
    private BigDecimal discount;

    @NotBlank
    private String code;

    @PositiveOrZero
    private BigDecimal minBasketValue;

}
