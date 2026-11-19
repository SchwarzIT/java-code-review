package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = BasketEntityConstants.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketEntity {

  @Id
  @Column(name = BasketEntityConstants.COLUMN_ID)
  private Long id;

  @Column(name = BasketEntityConstants.COLUMN_AMOUNT, precision = 10, scale = 2)
  private BigDecimal amount;

  @ManyToOne
  @JoinColumn(name = BasketEntityConstants.COLUMN_COUPON_CODE)
  private CouponEntity coupon;
}
