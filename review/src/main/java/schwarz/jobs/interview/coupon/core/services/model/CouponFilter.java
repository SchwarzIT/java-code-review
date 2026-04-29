package schwarz.jobs.interview.coupon.core.services.model;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CouponFilter {
    private Set<String> codes;
}
