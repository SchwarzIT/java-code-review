package schwarz.jobs.interview.coupon.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * The basket value the client sends when asking to apply a coupon.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketRequest {

    @NotNull
    @PositiveOrZero
    private BigDecimal value;

}
