package schwarz.jobs.interview.coupon.core.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * A coupon as stored in the database.
 */
@Entity
@Table(name = "coupons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, columnDefinition = "VARCHAR_IGNORECASE(250)")
    private String code;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "min_basket_value", precision = 10, scale = 2)
    private BigDecimal minBasketValue;

}
