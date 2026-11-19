package de.schwarz.jobs.interview.coupon.app.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.schwarz.jobs.interview.coupon.app.domain.exception.CouponNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.GetCouponOutPort;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCouponFunctionTest {

  @InjectMocks
  private GetCouponFunction getCouponFunction;

  @Mock
  private GetCouponOutPort getCouponOutPort;

  @Test
  void shouldReturnCouponByCode() {
    final String code = "1111";
    final Coupon expectedCoupon = Coupon.builder()
        .code(code)
        .discount(BigDecimal.TEN)
        .minBasketValue(BigDecimal.valueOf(50))
        .build();
    when(getCouponOutPort.findCouponByCode(code)).thenReturn(Optional.of(expectedCoupon));

    assertThat(getCouponFunction.getCoupon(code)).isSameAs(expectedCoupon);
    verify(getCouponOutPort).findCouponByCode(code);
  }

  @Test
  void shouldThrowCouponNotFoundWhenCodeDoesNotExist() {
    final String code = "missing";
    when(getCouponOutPort.findCouponByCode(code)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> getCouponFunction.getCoupon(code))
        .isInstanceOf(CouponNotFoundException.class)
        .hasMessage("Coupon not found");
  }

  @Test
  void shouldReturnCouponsForCodes() {
    final List<String> codes = List.of("1111", "2222");
    final List<Coupon> expectedCoupons = List.of(
        Coupon.builder().code("1111").discount(BigDecimal.TEN).build(),
        Coupon.builder().code("2222").discount(BigDecimal.ONE).build());
    when(getCouponOutPort.findAllCouponsByCodes(codes)).thenReturn(expectedCoupons);

    assertThat(getCouponFunction.getCoupons(codes)).isEqualTo(expectedCoupons);
    verify(getCouponOutPort).findAllCouponsByCodes(codes);
  }
}
