package schwarz.jobs.interview.coupon.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import schwarz.jobs.interview.coupon.CouponApplicationTests;
import schwarz.jobs.interview.coupon.common.util.MessageKey;
import schwarz.jobs.interview.coupon.core.services.model.Coupon;
import schwarz.jobs.interview.coupon.web.dto.AppResponseDto;
import schwarz.jobs.interview.coupon.web.dto.ApplyDiscountRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.BasketDTO;

public class AbstractWebTest extends CouponApplicationTests {

    protected Coupon coupon() {
        return Coupon.builder()
            .code(UUID.randomUUID().toString())
            .discount(BigDecimal.valueOf(Math.random()))
            .minBasketValue(BigDecimal.valueOf(Math.random()))
            .build();
    }

    protected Coupon coupon(final BigDecimal minBasketValue) {
        return Coupon.builder()
            .code(UUID.randomUUID().toString())
            .discount(BigDecimal.valueOf(Math.random()))
            .minBasketValue(minBasketValue)
            .build();
    }

    protected ApplyDiscountRequestDTO applyDiscountRequestDTO(final Coupon coupon, final BasketDTO basketDTO) {
        return ApplyDiscountRequestDTO.builder()
            .code(coupon.getCode())
            .basket(basketDTO)
            .build();
    }

    protected void assertResponse(final AppResponseDto actual,
                                  final MessageKey expectedMessage,
                                  final List<AppResponseDto.Error> errors) {

        assertThat(actual.message()).isEqualTo(messageService.message(expectedMessage.key()));
        assertThat(actual.code()).isEqualTo(expectedMessage.name());

        if (!errors.isEmpty()) {
            assertThat(actual.errors()).containsExactlyInAnyOrderElementsOf(errors);
        }
    }

    protected AppResponseDto.Error error(final String field, final MessageKey messageKey) {
        return AppResponseDto.Error.builder()
            .field(field)
            .message(messageService.message(messageKey.key()))
            .build();
    }
}
