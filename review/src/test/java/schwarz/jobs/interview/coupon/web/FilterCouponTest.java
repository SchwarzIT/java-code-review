package schwarz.jobs.interview.coupon.web;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static schwarz.jobs.interview.coupon.util.JsonUtils.toJson;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import schwarz.jobs.interview.coupon.common.util.MessageKey;
import schwarz.jobs.interview.coupon.core.services.model.Coupon;

public class FilterCouponTest extends AbstractWebTest {

    @Test
    @DisplayName("Filter coupons by coupon codes successfully")
    void validate_filter_coupons_by_coupon_codes_successfully() throws JSONException {
        // given
        final Coupon newCoupon01 = Coupon.builder()
            .code("code01")
            .discount(BigDecimal.valueOf(10))
            .minBasketValue(BigDecimal.valueOf(100))
            .build();

        final Coupon newCoupon02 = Coupon.builder()
            .code("code02")
            .discount(BigDecimal.valueOf(15))
            .minBasketValue(BigDecimal.valueOf(200))
            .build();

        couponService.createCoupon(newCoupon01);
        couponService.createCoupon(newCoupon02);

        final Coupon coupon01 = couponService.getCouponByCode(newCoupon01.getCode()).orElseThrow();
        final Coupon coupon02 = couponService.getCouponByCode(newCoupon02.getCode()).orElseThrow();

        // then
        assertEquals(
            toJson(filterCoupons(Set.of("code01", "code02"))),
            toJson(List.of(coupon01, coupon02)),
            STRICT);

        assertEquals(
            toJson(filterCoupons(Set.of("code01"))),
            toJson(List.of(coupon01)),
            STRICT);

        assertEquals(
            toJson(filterCoupons(Set.of("invalid-coupon-code"))),
            toJson(List.of()),
            STRICT);
    }

    @Test
//    @DisplayName("Filter coupons unsuccessfully for empty couppon codes")
    void validate_filter_coupons_unsuccessfully_for_empty_coupon_codes() {
        assertResponse(filterCouponsUnsuccessfully(Set.of()),
            MessageKey.APP_ERR_002,
            List.of(error("codes", MessageKey.COU_FIL_ERR_001)));

        assertResponse(filterCouponsUnsuccessfully(null),
            MessageKey.APP_ERR_002,
            List.of(error("codes", MessageKey.COU_FIL_ERR_001)));
    }
}
