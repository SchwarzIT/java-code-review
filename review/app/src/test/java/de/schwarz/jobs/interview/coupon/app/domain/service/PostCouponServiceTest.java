package de.schwarz.jobs.interview.coupon.app.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.UpsertCouponOutPort;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostCouponServiceTest {

  @InjectMocks
  private PostCouponService postCouponService;

  @Mock
  private UpsertCouponOutPort upsertCouponOutPort;

  @Test
  void should_test_create_coupon() {
    Coupon expectedCoupon = Coupon.builder()
        .code("1111")
        .discount(BigDecimal.TEN)
        .minBasketValue(BigDecimal.valueOf(50))
        .build();

    when(upsertCouponOutPort.upsertCoupon(expectedCoupon)).thenReturn(expectedCoupon);

    Coupon coupon = postCouponService.createCoupon(expectedCoupon);

    assertThat(coupon).isNotNull();
    assertThat(coupon).isEqualTo(expectedCoupon);
  }
}
