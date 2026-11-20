package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = CouponEntityConstants.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponEntity {

  @Id
  @Column(name = CouponEntityConstants.COLUMN_CODE)
  private String code;

  @Column(name = CouponEntityConstants.COLUMN_DISCOUNT, precision = 10, scale = 2)
  private BigDecimal discount;

  @Column(name = CouponEntityConstants.COLUMN_MIN_BASKET_VALUE, precision = 10, scale = 2)
  private BigDecimal minBasketValue;
}
