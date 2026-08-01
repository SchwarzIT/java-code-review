package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Set;

class CouponRequestDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_accept_non_empty_codes() {
        final CouponRequestDTO dto = CouponRequestDTO.builder()
                .codes(List.of("TEST1"))
                .build();
        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void should_reject_null_codes() {
        final CouponRequestDTO dto = CouponRequestDTO.builder()
                .codes(null)
                .build();
        final Set<ConstraintViolation<CouponRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void should_reject_empty_codes_list() {
        final CouponRequestDTO dto = CouponRequestDTO.builder()
                .codes(Collections.emptyList())
                .build();
        final Set<ConstraintViolation<CouponRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
    }
}
