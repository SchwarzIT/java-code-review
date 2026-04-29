package schwarz.jobs.interview.coupon.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import schwarz.jobs.interview.coupon.common.util.MessageKey;
import schwarz.jobs.interview.coupon.core.services.model.Coupon;
import schwarz.jobs.interview.coupon.web.dto.AppResponseDto;
import schwarz.jobs.interview.coupon.web.dto.ApplyDiscountRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.BasketDTO;

public class ApplyCouponForBasketTest extends AbstractWebTest {

    @Test
    @DisplayName("Apply Coupon successfully")
    void validate_apply_coupon_successfully() {
        // given
        final Long couponId = couponService.createCoupon(coupon(BigDecimal.valueOf(100)));
        final Coupon coupon = couponService.getCouponById(couponId).orElseThrow();

        final BasketDTO basketDTO = basketDTO(BigDecimal.valueOf(150.0));

        final ApplyDiscountRequestDTO applyDiscountRequestDTO = applyDiscountRequestDTO(coupon, basketDTO);

        // when
        final BasketDTO discountedBasket = applyCouponToBasket(applyDiscountRequestDTO)
            .returnResult(BasketDTO.class)
            .getResponseBody();

        // then
        assertThat(discountedBasket.getAppliedDiscount().compareTo(coupon.getDiscount())).isZero();

        final BigDecimal expectedBasketValueAfterDiscount = applyDiscountRequestDTO.getBasket().getValue().subtract(coupon.getDiscount());
        assertThat(discountedBasket.getValue().compareTo(expectedBasketValueAfterDiscount)).isZero();
    }

    @Test
    @DisplayName("Validate apply discount successfully when basket value is less than coupon minimum basket value")
    void validate_apply_discount_successfully_when_basket_value_is_less_than_coupon_min_basket_value() {
        // given
        final BasketDTO basketDTO = basketDTO(BigDecimal.valueOf(5.0));

        final Long couponId = couponService.createCoupon(coupon(BigDecimal.valueOf(10.0)));
        final Coupon coupon = couponService.getCouponById(couponId).orElseThrow();

        final ApplyDiscountRequestDTO applyDiscountRequestDTO = applyDiscountRequestDTO(coupon, basketDTO);

        // when
        final BasketDTO discountedBasket = applyCouponToBasket(applyDiscountRequestDTO)
            .returnResult(BasketDTO.class)
            .getResponseBody();

        // then
        // Since basket value is less than coupon's minimum basket value no discount is applied
        assertThat(discountedBasket.getValue().compareTo(applyDiscountRequestDTO.getBasket().getValue())).isZero();
    }

    @Test
    @DisplayName("Validate apply discount unsuccessfully when basket is null")
    void validate_apply_discount_unsuccessfully_when_basket_is_null() {
        // given
        final Coupon coupon = coupon();
        couponService.createCoupon(coupon);

        final ApplyDiscountRequestDTO applyDiscountRequestDTO = applyDiscountRequestDTO(coupon, null);

        // when
        final AppResponseDto responseDto = applyCouponToBasket(applyDiscountRequestDTO)
            .returnResult(AppResponseDto.class)
            .getResponseBody();

        // then
        assertResponse(responseDto, MessageKey.APP_ERR_002, List.of(error("basket", MessageKey.BAS_VAL_ERR_001)));
    }

    @Test
    @DisplayName("Validate apply discount unsuccessfully when basket and coupon code is null")
    void validate_apply_discount_unsuccessfully_when_basket_and_coupon_code_is_null() {
        // given
        final ApplyDiscountRequestDTO applyDiscountRequestDTO = ApplyDiscountRequestDTO.builder().build();

        // when
        final AppResponseDto responseDto = applyCouponToBasket(applyDiscountRequestDTO)
            .returnResult(AppResponseDto.class)
            .getResponseBody();

        // then
        assertResponse(responseDto,
            MessageKey.APP_ERR_002,
            List.of(error("basket", MessageKey.BAS_VAL_ERR_001), error("code", MessageKey.COU_VAL_ERR_001)));
    }

    @Test
    @DisplayName("Validate apply discount unsuccessfully when coupon code is invalid")
    void validate_apply_discount_unsuccessfully_when_coupon_code_is_invalid() {
        // given
        final BasketDTO basketDTO = basketDTO(BigDecimal.valueOf(10));
        final Coupon invalidCoupon = coupon();

        final ApplyDiscountRequestDTO applyDiscountRequestDTO = applyDiscountRequestDTO(invalidCoupon, basketDTO);

        // when
        final AppResponseDto responseDto = applyCouponToBasket(applyDiscountRequestDTO)
            .returnResult(AppResponseDto.class)
            .getResponseBody();

        // then
        assertResponse(responseDto, MessageKey.APP_ERR_002, List.of(error("code", MessageKey.COU_VAL_ERR_006)));
    }

    @Test
    @DisplayName("Validate apply discount unsuccessfully when basket value is negative")
    void validate_apply_discount_unsuccessfully_when_basket_value_is_negative() {
        // given
        final BasketDTO basketDTO = basketDTO(BigDecimal.valueOf(-150.0));

        final Coupon coupon = coupon();
        couponService.createCoupon(coupon);

        final ApplyDiscountRequestDTO applyDiscountRequestDTO = applyDiscountRequestDTO(coupon, basketDTO);

        // when
        final AppResponseDto responseDto = applyCouponToBasket(applyDiscountRequestDTO)
            .returnResult(AppResponseDto.class)
            .getResponseBody();

        // then
        assertResponse(responseDto, MessageKey.APP_ERR_002, List.of(error("basket.value", MessageKey.BAS_VAL_ERR_002)));
    }

    private BasketDTO basketDTO(final BigDecimal value) {
        return BasketDTO.builder()
            .value(value)
            .appliedDiscount(BigDecimal.valueOf(5.0))
            .build();
    }
}
