package de.schwarz.jobs.interview.coupon.app.configuration;

import de.schwarz.jobs.interview.coupon.app.domain.exception.BasketNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.exception.CouponNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerConfiguration {

  @ExceptionHandler({BasketNotFoundException.class, CouponNotFoundException.class})
  public ResponseEntity<ProblemDetail> handleNotFound(RuntimeException exception) {
    log.warn("Request failed because the requested resource was not found", exception);
    return problem(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintValidation(ConstraintViolationException exception) {
    log.warn("Constraint violation occurred", exception);
    List<String> errors = exception.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .sorted()
        .toList();
    return handleValidation(errors);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ProblemDetail> handleMethodValidation(HandlerMethodValidationException exception) {
    log.warn("Request parameter validation failed", exception);
    return handleValidation(List.of("Invalid request parameters"));
  }

  private ResponseEntity<ProblemDetail> handleValidation(List<String> errors) {
    log.warn("Request validation failed with {} error(s)", errors.size());
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed");
    problem.setProperty("errors", errors);
    return ResponseEntity.badRequest().body(problem);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ProblemDetail> handleUnreadableRequest(HttpMessageNotReadableException exception) {
    log.warn("Request body could not be read", exception);
    return problem(HttpStatus.BAD_REQUEST, "Request body is invalid");
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ProblemDetail> handleBindingValidation(BindException exception) {
    log.warn("Bind exception occurred", exception);
    List<String> errors = exception.getBindingResult().getAllErrors().stream()
        .map(this::formatValidationError)
        .toList();
    return handleValidation(errors);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ProblemDetail> handleUnauthorized(AuthenticationException exception) {
    log.warn("Request rejected because authentication is missing or invalid", exception);
    return problem(HttpStatus.UNAUTHORIZED, "Authentication is required");
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ProblemDetail> handleForbidden(AccessDeniedException exception) {
    log.warn("Request rejected because access is denied", exception);
    return problem(HttpStatus.FORBIDDEN, "Access is denied");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleUnexpected(Exception exception) {
    log.error("Unexpected error while handling request", exception);
    return problem(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
  }

  private String formatValidationError(ObjectError error) {
    String message = error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage();
    String field = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
    return field + ": " + message;
  }

  private ResponseEntity<ProblemDetail> problem(HttpStatus status, String detail) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    return ResponseEntity.status(status).body(problem);
  }
}
