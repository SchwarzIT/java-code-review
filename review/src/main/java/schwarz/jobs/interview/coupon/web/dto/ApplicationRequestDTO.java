package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import schwarz.jobs.interview.coupon.core.services.model.Basket;

@Data
@Builder
public class ApplicationRequestDTO {

    @NotBlank
    private String code;

    @NotNull
    private Basket basket;

}
