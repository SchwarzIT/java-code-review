package schwarz.jobs.interview.infrastructure.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import schwarz.jobs.interview.domain.model.CreateCouponRequest;

@Data
@Builder
public class CreateCouponRequestDTO {
    @NotNull
    private String code;

    @NotNull
    private BigDecimal discount;

    @NotNull
    private BigDecimal minBasketValue;

    public CreateCouponRequest toDomainModel() {
        return new CreateCouponRequest(
                this.code,
                this.discount,
                this.minBasketValue
        );
    }
}
