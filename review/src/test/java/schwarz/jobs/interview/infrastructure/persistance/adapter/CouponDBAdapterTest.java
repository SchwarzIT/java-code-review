package schwarz.jobs.interview.infrastructure.persistance.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import schwarz.jobs.interview.domain.exception.BadRequestException;
import schwarz.jobs.interview.domain.exception.NotFoundException;
import schwarz.jobs.interview.domain.model.Basket;
import schwarz.jobs.interview.domain.model.Coupon;
import schwarz.jobs.interview.domain.model.CreateCouponRequest;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;
import schwarz.jobs.interview.infrastructure.persistance.jpa.repository.CouponDBRepository;

@ExtendWith(SpringExtension.class)
class CouponDBAdapterTest {

    @InjectMocks
    private CouponDBAdapter couponAdapter;

    @Mock
    private CouponDBRepository couponRepository;

    private final EasyRandom easyRandom = new EasyRandom();

    @AfterEach
    void tearDown() {
        clearInvocations(couponRepository);
    }

    @Test
    void createCouponTest() {
        // Given
        CreateCouponRequest dto = easyRandom.nextObject(CreateCouponRequest.class);

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
    void applyCouponTest() {
        // GIVEN
        final Basket basket = new Basket(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(30),
                false,
                BigDecimal.ZERO
        );
        String code = easyRandom.nextObject(String.class);
        Optional<CouponEntity> optionalCoupon = Optional.of(
                new CouponEntity(
                        1L,
                        code,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(50)
                )
        );

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

    @Test
    void applyCouponNotFoundException() {
        // GIVEN
        final Basket basket = new Basket(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(30),
                false,
                BigDecimal.ZERO
        );
        String code = easyRandom.nextObject(String.class);

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NotFoundException.class, () -> couponAdapter.applyCoupon(basket, code));
        verify(couponRepository, times(1)).findByCodeIgnoreCase(code);
    }

    @Test
    void applyCouponMinBasketValueBadRequestException() {
        // GIVEN
        final Basket basket = new Basket(
                BigDecimal.valueOf(40),
                BigDecimal.valueOf(30),
                false,
                BigDecimal.ZERO
        );
        String code = easyRandom.nextObject(String.class);
        Optional<CouponEntity> optionalCoupon = Optional.of(
                new CouponEntity(
                        1L,
                        code,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(50) // Min basket value > basket value
                )
        );

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(optionalCoupon);

        // WHEN & THEN
        assertThrows(BadRequestException.class, () -> couponAdapter.applyCoupon(basket, code));
        verify(couponRepository, times(1)).findByCodeIgnoreCase(code);
    }

    @Test
    void applyCouponNegativeValueBadRequestException() {
        // GIVEN
        final Basket basket = new Basket(
                BigDecimal.valueOf(-100),
                BigDecimal.valueOf(30),
                false,
                BigDecimal.ZERO
        );
        String code = easyRandom.nextObject(String.class);
        Optional<CouponEntity> optionalCoupon = Optional.of(
                new CouponEntity(
                        1L,
                        code,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(50)
                )
        );

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(optionalCoupon);

        // WHEN & THEN
        assertThrows(BadRequestException.class, () -> couponAdapter.applyCoupon(basket, code));
        verify(couponRepository, times(1)).findByCodeIgnoreCase(code);
    }

    @Test
    void applyCouponDiscountOutOfRangeBadRequestException() {
        // GIVEN
        final Basket basket = new Basket(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(150), // Invalid discount > 100
                false,
                BigDecimal.ZERO
        );
        String code = easyRandom.nextObject(String.class);
        Optional<CouponEntity> optionalCoupon = Optional.of(
                new CouponEntity(
                        1L,
                        code,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(50)
                )
        );

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(optionalCoupon);

        // WHEN & THEN
        assertThrows(BadRequestException.class, () -> couponAdapter.applyCoupon(basket, code));
        verify(couponRepository, times(1)).findByCodeIgnoreCase(code);
    }

    @Test
    void getCouponsTest() {
        // Given
        List<String> codes = easyRandom.objects(String.class, 3).toList();
        CouponEntity coupon = easyRandom.nextObject(CouponEntity.class);
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
