package de.schwarz.jobs.interview.coupon.api.coupons.v1.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebCreateBasket {

  @NotNull
  private BigDecimal amount;
}
