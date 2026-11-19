package de.schwarz.jobs.interview.coupon.api.coupons.v1.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class WebCoupon {

  @NotNull
  @NotEmpty
  private String code;
  @NotNull
  private BigDecimal discount;
  @NotNull
  private BigDecimal minBasketValue;
}
