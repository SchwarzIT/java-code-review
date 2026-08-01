package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CouponDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_accept_valid_coupon() {
        final CouponDTO dto = validCoupon().build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void should_reject_blank_code() {
        final CouponDTO dto = validCoupon().code("").build();

        assertThat(propertyPaths(validator.validate(dto))).containsExactly("code");
    }

    @Test
    void should_reject_null_discount() {
        final CouponDTO dto = validCoupon().discount(null).build();

        assertThat(propertyPaths(validator.validate(dto))).containsExactly("discount");
    }

    @Test
    void should_reject_negative_discount() {
        final CouponDTO dto = validCoupon().discount(BigDecimal.valueOf(-5)).build();

        assertThat(propertyPaths(validator.validate(dto))).containsExactly("discount");
    }

    @Test
    void should_reject_negative_minBasketValue() {
        final CouponDTO dto = validCoupon().minBasketValue(BigDecimal.valueOf(-1)).build();

        assertThat(propertyPaths(validator.validate(dto))).containsExactly("minBasketValue");
    }

    private CouponDTO.CouponDTOBuilder validCoupon() {
        return CouponDTO.builder()
                .code("TEST1")
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(50));
    }

    private Set<String> propertyPaths(final Set<ConstraintViolation<CouponDTO>> violations) {
        return violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());
    }
}
