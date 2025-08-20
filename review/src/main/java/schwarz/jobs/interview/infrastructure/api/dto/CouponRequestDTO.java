package schwarz.jobs.interview.infrastructure.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponRequestDTO {

    @NotNull
    private List<String> codes;

}
