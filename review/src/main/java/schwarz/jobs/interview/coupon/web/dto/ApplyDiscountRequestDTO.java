package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import schwarz.jobs.interview.coupon.common.validator.ValidCouponCode;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApplyDiscountRequestDTO {

    @NotBlank(message = "{coupon.code.required}")
    @ValidCouponCode(message = "{coupon.code.invalid}")
    private String code;

    @NotNull(message = "{basket.required}")
    @Valid
    private BasketDTO basket;
}
