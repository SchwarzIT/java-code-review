package schwarz.jobs.interview.coupon.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A coupon as returned to API clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {

    private BigDecimal discount;

    private String code;

    private BigDecimal minBasketValue;

}
