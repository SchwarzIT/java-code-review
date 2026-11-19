package de.schwarz.jobs.interview.coupon.app.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

  private String code;
  private BigDecimal discount;
  private BigDecimal minBasketValue;
}
