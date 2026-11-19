package de.schwarz.jobs.interview.coupon.app.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.schwarz.jobs.interview.coupon.app.domain.exception.CouponNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetCouponServiceTest {

  @InjectMocks
  private GetCouponService getCouponService;

  @Mock
  private GetCouponFunction getCouponFunction;

  @Test
  void should_test_get_coupon() {
    Coupon expectedCoupon = Coupon.builder()
        .code("1111")
        .discount(BigDecimal.TEN)
        .minBasketValue(BigDecimal.valueOf(50))
        .build();

    when(getCouponFunction.getCoupon("1111")).thenReturn(expectedCoupon);

    Coupon coupon = getCouponService.getCoupon("1111");

    assertThat(coupon).isNotNull();
    assertThat(coupon).isEqualTo(expectedCoupon);
  }

  @Test
  void should_test_get_coupon_not_found() {
    when(getCouponFunction.getCoupon("1111")).thenThrow(new CouponNotFoundException("1111"));
    Assertions.assertThatThrownBy(() -> getCouponService.getCoupon("1111")).isInstanceOf(CouponNotFoundException.class);
  }

  @Test
  void should_test_get_coupons() {
    Coupon expectedCoupon1 = Coupon.builder()
        .code("1111")
        .discount(BigDecimal.TEN)
        .minBasketValue(BigDecimal.valueOf(50))
        .build();
    Coupon expectedCoupon2 = Coupon.builder()
        .code("2222")
        .discount(BigDecimal.TEN)
        .minBasketValue(BigDecimal.valueOf(50))
        .build();
    List<String> codes = List.of("1111", "2222");
    List<Coupon> expectedCoupons = List.of(expectedCoupon1, expectedCoupon2);

    when(getCouponFunction.getCoupons(codes)).thenReturn(expectedCoupons);

    List<Coupon> coupons = getCouponService.getCoupons(codes);

    assertThat(coupons).isEqualTo(expectedCoupons);
  }
}
