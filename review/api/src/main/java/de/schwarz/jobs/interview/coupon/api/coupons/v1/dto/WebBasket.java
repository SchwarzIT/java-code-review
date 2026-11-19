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
public class WebBasket {

  @NotNull
  private Long id;
  @NotNull
  private BigDecimal amount;
  private WebCoupon coupon;
  private boolean applicationSuccessful;
}
