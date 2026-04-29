package schwarz.jobs.interview.coupon.core.services.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Coupon {
    private Long id;
    private String code;
    private BigDecimal discount;
    private BigDecimal minBasketValue;
}
