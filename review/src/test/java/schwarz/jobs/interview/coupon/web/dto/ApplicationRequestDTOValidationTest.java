package schwarz.jobs.interview.coupon.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import schwarz.jobs.interview.coupon.core.domain.Basket;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationRequestDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_accept_valid_basket_and_filled_code() {
        final ApplicationRequestDTO dto = ApplicationRequestDTO.builder()
                .basket(validBasket().build())
                .code("12345")
                .build();

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void should_not_accept_invalid_basket() {
        final ApplicationRequestDTO dto = ApplicationRequestDTO.builder()
                .basket(validBasket().value(BigDecimal.valueOf(-1)).build())
                .code("12345")
                .build();

        assertThat(propertyPaths(validator.validate(dto))).containsExactly("basket.value");
    }

    @Test
    void should_not_accept_blank_code() {
        final ApplicationRequestDTO dto = ApplicationRequestDTO.builder()
                .basket(validBasket().build())
                .code("")
                .build();

        assertThat(propertyPaths(validator.validate(dto))).containsExactly("code");
    }

    private Basket.BasketBuilder validBasket() {
        return Basket.builder()
                .value(BigDecimal.TEN)
                .appliedDiscount(BigDecimal.TEN)
                .applicationSuccessful(false);
    }

    private Set<String> propertyPaths(final Set<ConstraintViolation<ApplicationRequestDTO>> violations) {
        return violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());
    }

}
