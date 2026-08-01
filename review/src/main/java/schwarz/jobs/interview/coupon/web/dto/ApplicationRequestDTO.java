package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Basket;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequestDTO {

    @NotBlank
    private String code;

    @NotNull
    @Valid
    private Basket basket;

}
