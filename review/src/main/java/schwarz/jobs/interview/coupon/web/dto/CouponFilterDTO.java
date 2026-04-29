package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CouponFilterDTO {

    @NotEmpty(message = "{coupon.filter.codes.required}")
    private Set<String> codes;
}
