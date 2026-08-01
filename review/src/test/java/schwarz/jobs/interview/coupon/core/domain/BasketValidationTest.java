package schwarz.jobs.interview.coupon.core.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import schwarz.jobs.interview.coupon.core.domain.Basket;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BasketValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_reject_negative_basket_value() {
        final Basket basket = Basket.builder().value(BigDecimal.valueOf(-1)).build();

        final Set<ConstraintViolation<Basket>> violations = validator.validate(basket);

        assertThat(violations).isNotEmpty();
    }
}
