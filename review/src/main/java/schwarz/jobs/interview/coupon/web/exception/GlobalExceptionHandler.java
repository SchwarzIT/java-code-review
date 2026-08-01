package schwarz.jobs.interview.coupon.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import schwarz.jobs.interview.coupon.core.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.core.exception.DuplicateCouponException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCouponException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCoupon(final DuplicateCouponException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundCoupon(final CouponNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(final Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(final MethodArgumentNotValidException ex) {

        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), message));
    }

    public record ErrorResponse(Instant timestamp, int status, String message) {}
}
