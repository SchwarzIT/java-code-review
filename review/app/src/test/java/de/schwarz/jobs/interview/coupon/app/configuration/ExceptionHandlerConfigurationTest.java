package de.schwarz.jobs.interview.coupon.app.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import de.schwarz.jobs.interview.coupon.app.domain.exception.BasketNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.exception.CouponNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

class ExceptionHandlerConfigurationTest {

  private final ExceptionHandlerConfiguration exceptionHandlerConfiguration = new ExceptionHandlerConfiguration();

  @Test
  void shouldReturnNotFoundForMissingBasket() {
    ResponseEntity<ProblemDetail> response =
        exceptionHandlerConfiguration.handleNotFound(new BasketNotFoundException("Basket not found"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isEqualTo("Basket not found");
  }

  @Test
  void shouldReturnNotFoundForMissingCoupon() {
    ResponseEntity<ProblemDetail> response =
        exceptionHandlerConfiguration.handleNotFound(new CouponNotFoundException("Coupon not found"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isEqualTo("Coupon not found");
  }

  @Test
  void shouldReturnBadRequestForValidationErrors() {
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
    bindingResult.addError(new FieldError("request", "value", "must be greater than zero"));

    ResponseEntity<ProblemDetail> response =
        exceptionHandlerConfiguration.handleBindingValidation(new BindException(bindingResult));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isEqualTo("Request validation failed");
    assertThat(response.getBody().getProperties()).containsKey("errors");
  }

  @Test
  void shouldReturnUnauthorizedForAuthenticationErrors() {
    ResponseEntity<ProblemDetail> response =
        exceptionHandlerConfiguration.handleUnauthorized(new BadCredentialsException("bad token"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isEqualTo("Authentication is required");
  }

  @Test
  void shouldReturnForbiddenForAccessDenied() {
    ResponseEntity<ProblemDetail> response =
        exceptionHandlerConfiguration.handleForbidden(new AccessDeniedException("denied"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  @Test
  void shouldReturnInternalServerErrorForUnexpectedErrors() {
    ResponseEntity<ProblemDetail> response =
        exceptionHandlerConfiguration.handleUnexpected(new IllegalStateException("internal detail"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isEqualTo("An unexpected error occurred");
  }
}
