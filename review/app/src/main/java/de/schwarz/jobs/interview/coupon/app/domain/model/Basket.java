package de.schwarz.jobs.interview.coupon.app.domain.model;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Basket {

  @NotNull
  private Long id;
  @NotNull
  private BigDecimal amount;
  private Coupon coupon;

  public void applyDiscount(final Coupon coupon) {
    this.coupon = isDiscountValid(coupon.getDiscount(), coupon.getMinBasketValue()) ? coupon : null;
  }

  private boolean isDiscountValid(final BigDecimal discount, final BigDecimal minBasketValue) {
    if (discount == null) {
      return false;
    }
    return discount.compareTo(BigDecimal.ZERO) >= 0 && minBasketValueIsMet(minBasketValue);
  }

  private boolean minBasketValueIsMet(final BigDecimal minBasketValue) {
    return minBasketValue != null && amount.compareTo(minBasketValue) >= 0;
  }

  public boolean isApplicationSuccessful() {
    return coupon != null;
  }
}
