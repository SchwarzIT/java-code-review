package schwarz.jobs.interview.coupon.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.APP_ERR_002;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.COUPON_CREATED;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.COU_VAL_ERR_001;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.COU_VAL_ERR_002;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.COU_VAL_ERR_004;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import schwarz.jobs.interview.coupon.web.dto.AppResponseDto;
import schwarz.jobs.interview.coupon.web.dto.CreateCouponDTO;

public class CreateCouponTest extends AbstractWebTest {

    @Test
    @DisplayName("Create Coupon Successfully")
    void validate_create_coupon_successfully() {
        // given
        final CreateCouponDTO createCouponDTO = CreateCouponDTO.builder()
            .code("cou_code")
            .discount(BigDecimal.valueOf(50.0))
            .minBasketValue(BigDecimal.valueOf(15.0))
            .build();

        // when
        final EntityExchangeResult<AppResponseDto> exchange = createCoupon(createCouponDTO)
            .returnResult(AppResponseDto.class);

        // then
        assertThat(exchange.getStatus()).isEqualTo(CREATED);
        assertResponse(exchange.getResponseBody(), COUPON_CREATED, Collections.emptyList());
    }

    @Test
    @DisplayName("Create Coupon unsuccessfully, when coupon code is missing")
    void validate_create_coupon_unsuccessfully_when_coupon_code_is_missing() {
        // given
        final CreateCouponDTO createCouponDTO = CreateCouponDTO.builder()
            .discount(BigDecimal.valueOf(50.0))
            .minBasketValue(BigDecimal.valueOf(15.0))
            .build();

        // when
        final EntityExchangeResult<AppResponseDto> exchange =
            createCoupon(createCouponDTO).returnResult(AppResponseDto.class);

        // then
        assertThat(exchange.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertResponse(exchange.getResponseBody(), APP_ERR_002, List.of(error("code", COU_VAL_ERR_001)));
    }

    @Test
    @DisplayName("Create Coupon unsuccessfully, when coupon discount is missing and minimum basket value is missing")
    void validate_create_coupon_unsuccessfully_when_coupon_discount_is_missing_and_minimum_basket_value_is_missing() {
        // given
        final CreateCouponDTO createCouponDTO = CreateCouponDTO.builder().code("any-code").build();

        // when
        final EntityExchangeResult<AppResponseDto> exchange =
            createCoupon(createCouponDTO).returnResult(AppResponseDto.class);

        // then
        assertThat(exchange.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertResponse(exchange.getResponseBody(),
            APP_ERR_002,
            List.of(error("discount", COU_VAL_ERR_002), error("minBasketValue", COU_VAL_ERR_004)));
    }
}
