package schwarz.jobs.interview.coupon.web.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponRequestDTO {

    @NotNull
    private List<String> codes;

}
