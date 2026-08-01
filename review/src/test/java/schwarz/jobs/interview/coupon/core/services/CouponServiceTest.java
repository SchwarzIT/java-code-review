package schwarz.jobs.interview.coupon.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.core.exception.DuplicateCouponException;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.domain.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Nested
    class CreateCoupon {
        @Test
        void should_create_coupon_when_data_is_valid() {
            final CouponDTO dto = CouponDTO.builder()
                    .code("12345")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(BigDecimal.valueOf(50))
                    .build();

            when(couponRepository.findByCode("12345")).thenReturn(Optional.empty());

            couponService.createCoupon(dto);

            final ArgumentCaptor<Coupon> captor = ArgumentCaptor.forClass(Coupon.class);
            verify(couponRepository,times(1)).save(captor.capture());

            assertThat(captor.getValue().getCode()).isEqualTo("12345");
            assertThat(captor.getValue().getDiscount()).isEqualTo(BigDecimal.TEN);
            assertThat(captor.getValue().getMinBasketValue()).isEqualTo(BigDecimal.valueOf(50));
        }

        @Test
        void should_throw_when_coupon_code_already_exists() {
            final CouponDTO dto = CouponDTO.builder()
                    .code("12345")
                    .discount(BigDecimal.TEN)
                    .build();

            when(couponRepository.findByCode("12345")).thenReturn(Optional.of(Coupon.builder().code("12345").build()));

            assertThatThrownBy(() -> couponService.createCoupon(dto))
                    .isInstanceOf(DuplicateCouponException.class);

            verify(couponRepository, never()).save(any());
        }
    }


    @Nested
    class Apply {
        @Test
        void should_apply_discount_when_basket_qualifies() {
            final Basket basket = Basket.builder()
                    .value(BigDecimal.valueOf(100))
                    .build();

            when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
                    .code("1111")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(BigDecimal.valueOf(50))
                    .build()));

            final Basket optionalBasket = couponService.apply(basket, "1111");

            assertThat(optionalBasket.getAppliedDiscount()).isEqualTo(BigDecimal.TEN);
            assertThat(optionalBasket.getApplicationSuccessful()).isTrue();
        }

        @Test
        void should_not_apply_discount_when_basket_value_is_zero() {
            final Basket basket = Basket.builder()
                    .value(BigDecimal.ZERO)
                    .build();

            when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
                    .code("1111")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(BigDecimal.valueOf(50))
                    .build()));

            final Basket result = couponService.apply(basket, "1111");

            assertThat(result.getApplicationSuccessful()).isFalse();
            assertThat(result).isEqualTo(basket);
            assertThat(result.getAppliedDiscount()).isNull();
        }

        @Test
        void should_not_apply_discount_when_basket_below_minimum() {
            final Basket basket = Basket.builder()
                    .value(BigDecimal.valueOf(30))
                    .build();

            when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
                    .code("1111")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(BigDecimal.valueOf(50))
                    .build()));

            final Basket result = couponService.apply(basket, "1111");

            assertThat(result.getApplicationSuccessful()).isFalse();
            assertThat(result).isEqualTo(basket);
            assertThat(result.getAppliedDiscount()).isNull();

        }

        @Test
        void should_apply_discount_when_coupon_has_no_minimum() {
            final Basket basket = Basket.builder()
                    .value(BigDecimal.valueOf(10))
                    .build();

            when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
                    .code("1111")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(null)
                    .build()));

            final Basket result = couponService.apply(basket, "1111");

            assertThat(result.getApplicationSuccessful()).isTrue();
        }

        @Test
        void should_throw_when_coupon_not_found(){
            final Basket basket = Basket.builder()
                    .value(BigDecimal.valueOf(100))
                    .build();

            when(couponRepository.findByCode("does-not-exist")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> couponService.apply(basket, "does-not-exist"))
                    .isInstanceOf(CouponNotFoundException.class);

        }
    }

    @Nested
    class GetCoupons {

        @Test
        void should_return_coupons_for_given_codes() {
            CouponRequestDTO dto = CouponRequestDTO.builder()
                    .codes(Arrays.asList("1111", "1234"))
                    .build();

            when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
                    .code("1111")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(BigDecimal.valueOf(50))
                    .build()));
            when(couponRepository.findByCode("1234")).thenReturn(Optional.of(Coupon.builder()
                    .code("1234")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(BigDecimal.valueOf(50))
                    .build()));

            List<CouponDTO> returnedCoupons = couponService.getCoupons(dto);

            assertThat(returnedCoupons.get(0).getCode()).isEqualTo("1111");
            assertThat(returnedCoupons.get(1).getCode()).isEqualTo("1234");
        }

        @Test
        void should_throw_when_code_not_found() {
            CouponRequestDTO dto = CouponRequestDTO.builder()
                    .codes(List.of("does-not-exist"))
                    .build();

            when(couponRepository.findByCode("does-not-exist")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> couponService.getCoupons(dto))
                    .isInstanceOf(CouponNotFoundException.class);
        }
    }

}
