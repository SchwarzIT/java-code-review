package de.schwarz.jobs.interview.coupon.app.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.schwarz.jobs.interview.coupon.app.domain.exception.CouponNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.UpsertBasketOutPort;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostBasketDiscountServiceTest {

  @InjectMocks
  private PostBasketDiscountService postBasketDiscountService;

  @Mock
  private GetBasketFunction getBasketFunction;

  @Mock
  private GetCouponFunction getCouponFunction;

  @Mock
  private UpsertBasketOutPort upsertBasketOutPort;

  @Test
  void shouldApplyDiscountAndPersistBasket() {
    final Long basketId = 1L;
    final String testCouponCode = "1111";
    final Coupon coupon = Coupon.builder()
        .code(testCouponCode)
        .discount(BigDecimal.TEN)
        .minBasketValue(BigDecimal.valueOf(50))
        .build();
    final Basket basket = Basket.builder()
        .id(basketId)
        .amount(BigDecimal.valueOf(100))
        .build();

    when(getBasketFunction.getBasket(basketId)).thenReturn(basket);
    when(getCouponFunction.getCoupon(testCouponCode)).thenReturn(coupon);
    when(upsertBasketOutPort.upsertBasket(basket)).thenReturn(basket);

    Basket basketWithAppliedDiscount = postBasketDiscountService.applyDiscount(basketId, testCouponCode);

    assertThat(basketWithAppliedDiscount.isApplicationSuccessful()).isTrue();
    assertThat(basketWithAppliedDiscount.getCoupon()).isEqualTo(coupon);
    verify(upsertBasketOutPort).upsertBasket(basket);
  }

  @Test
  void shouldNotApplyNegativeDiscount() {
    final Long basketId = 1L;
    final String testCouponCode = "1111";
    final Coupon coupon = Coupon.builder()
        .code(testCouponCode)
        .discount(BigDecimal.valueOf(-10))
        .minBasketValue(BigDecimal.valueOf(50))
        .build();
    final Basket basket = Basket.builder()
        .id(basketId)
        .amount(BigDecimal.valueOf(100))
        .build();

    when(getBasketFunction.getBasket(basketId)).thenReturn(basket);
    when(getCouponFunction.getCoupon(testCouponCode)).thenReturn(coupon);
    when(upsertBasketOutPort.upsertBasket(basket)).thenReturn(basket);

    Basket basketWithAppliedDiscount = postBasketDiscountService.applyDiscount(basketId, testCouponCode);

    assertThat(basketWithAppliedDiscount.isApplicationSuccessful()).isFalse();
    assertThat(basketWithAppliedDiscount.getCoupon()).isNull();
    verify(upsertBasketOutPort).upsertBasket(basket);
  }

  @Test
  void shouldNotPersistWhenCouponIsNotFound() {
    final Long basketId = 1L;
    String testCouponCode = "1111";
    when(getBasketFunction.getBasket(basketId)).thenReturn(Basket.builder()
        .id(basketId)
        .amount(BigDecimal.valueOf(100))
        .build());
    when(getCouponFunction.getCoupon(testCouponCode)).thenThrow(new CouponNotFoundException("Coupon not found"));

    assertThatThrownBy(() -> postBasketDiscountService.applyDiscount(basketId, testCouponCode))
        .isInstanceOf(CouponNotFoundException.class);
  }
}
