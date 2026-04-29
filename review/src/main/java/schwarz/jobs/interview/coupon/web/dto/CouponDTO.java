package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDTO {
    private Long id;
    private BigDecimal discount;
    private String code;
    private BigDecimal minBasketValue;
}
