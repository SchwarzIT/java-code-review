package schwarz.jobs.interview.coupon.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * What we send back after trying to apply a coupon to a basket.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketResponse {

    private BigDecimal value;

    private BigDecimal appliedDiscount;

    private BigDecimal finalValue;

    private boolean applicationSuccessful;

}
