package schwarz.jobs.interview.coupon.core.domain;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "min_basket_value", precision = 10, scale = 2)
    private BigDecimal minBasketValue;

}
