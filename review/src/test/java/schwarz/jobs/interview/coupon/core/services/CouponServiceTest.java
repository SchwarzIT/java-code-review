package schwarz.jobs.interview.coupon.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import schwarz.jobs.interview.application.dto.CouponDTO;
import schwarz.jobs.interview.domain.model.Basket;
import schwarz.jobs.interview.domain.model.Coupon;
import schwarz.jobs.interview.domain.model.CreateCouponRequest;
import schwarz.jobs.interview.infrastructure.persistance.adapter.CouponDBAdapter;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;
import schwarz.jobs.interview.infrastructure.persistance.jpa.repository.CouponDBRepository;

@ExtendWith(SpringExtension.class)
public class CouponServiceTest {

    // TODO revisar c/test porque asi como esta no aplica GHERKIN (Given, When, Then)

    @InjectMocks
    private CouponDBAdapter couponAdapter;

    @Mock
    private CouponDBRepository couponRepository;

    @Test
    public void createCouponTest() {
        // Given
        CreateCouponRequest dto = new CreateCouponRequest(
            "12345",
            BigDecimal.TEN,
            BigDecimal.valueOf(50)
        );

        CouponEntity couponEntity = new CouponEntity(
            1L,
                dto.getCode(),
            dto.getDiscount(),
            dto.getMinBasketValue()
        );

        Coupon coupon = new Coupon(
            dto.getCode(),
            dto.getDiscount(),
            dto.getMinBasketValue()
        );

        when(couponRepository.save(any())).thenReturn(couponEntity);

        // When
        Coupon result = couponAdapter.createCoupon(dto);

        // Then
        verify(couponRepository, times(1)).save(any());
        assertEquals(coupon, result);
    }

    @Test
    public void test_apply_coupon_method() {
        // GIVEN
        final Basket basket = new Basket(
            BigDecimal.valueOf(100),
            BigDecimal.valueOf(30),
            false,
            BigDecimal.ZERO
        );
        String code = "1111";
        Optional<CouponEntity> optionalCoupon = Optional.of(CouponEntity.builder()
                .code(code)
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(50))
                .build());

        Basket expectedBasket = new Basket(
            basket.getValue(),
            basket.getAppliedDiscount(),
            true,
            calculateDiscountedValue(basket.getValue(), basket.getAppliedDiscount())
        );

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(optionalCoupon);

        // WHEN
        Basket result = couponAdapter.applyCoupon(basket, code);

        // THEN
        assertEquals(expectedBasket, result);
        verify(couponRepository, times(1)).findByCodeIgnoreCase(code);
    }

    // TODO Add tests cases for exceptions.

    @Test
    public void getCouponsTest() {
        // Given
        List<String> codes = Arrays.asList("1111", "1234");
        CouponEntity coupon = new CouponEntity(
            1L,
            "1111",
            BigDecimal.TEN,
            BigDecimal.valueOf(50)
        );
        List<CouponEntity> coupons = List.of(coupon);

        List<Coupon> expectedCoupons = coupons.stream().map(couponEntity ->
                new Coupon(
                        couponEntity.getCode(),
                        couponEntity.getDiscount(),
                        couponEntity.getMinBasketValue()
                )
        ).toList();

        when(couponRepository.findByCodeIgnoreCaseIn(codes)).thenReturn(coupons);

        // When
        List<Coupon> returnedCoupons = couponAdapter.getCoupons(codes);

        // Then
        assertEquals(expectedCoupons, returnedCoupons);
        verify(couponRepository, times(1)).findByCodeIgnoreCaseIn(codes);
    }

    private BigDecimal calculateDiscountedValue(BigDecimal value, BigDecimal discount) {
        return value.subtract(
                value.multiply(discount.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP))
        ).setScale(2, RoundingMode.DOWN);
    }
}
